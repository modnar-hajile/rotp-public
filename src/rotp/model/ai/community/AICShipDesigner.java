/*
 * Copyright 2015-2020 Ray Fowler
 * 
 * Licensed under the GNU General Public License, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     https://www.gnu.org/licenses/gpl-3.0.html
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package rotp.model.ai.community;

import java.util.ArrayList;
import java.util.List;
import rotp.model.ai.interfaces.ShipDesigner;
import rotp.model.empires.Empire;
import rotp.model.galaxy.ShipFleet;
import rotp.model.galaxy.StarSystem;
import rotp.model.ships.ShipDesign;
import rotp.model.ships.ShipDesignLab;
import rotp.model.ships.ShipSpecial;
import rotp.model.ships.ShipSpecialColony;
import rotp.model.ships.ShipWeapon;
import rotp.util.Base;

public class AICShipDesigner implements Base, ShipDesigner {
    private static final int OBS_DESTROYER_TURNS = 20;
    private static final int OBS_FIGHTER_TURNS = 16;
    private static final int OBS_BOMBER_TURNS = 12;
    private static final int OBS_COLONY_TURNS = 8;
    private static final int OBS_SCOUT_TURNS = 1;
	
	// modnar: scale military ship obsolete turn counts using the current turn
	// attempt to allow AI more opportunities to use fleet before scraping
	// (currentTurn + 100) / (currentTurn + 40))
	// approx. 2.5 times as long before obsolete at Turn 1
	// approx. 2.0 times as long before obsolete at Turn 20
	// approx. 1.5 times as long before obsolete at Turn 80
	// approx. 1.25 times as long before obsolete at Turn 200
	private int currentTurn = galaxy().currentTurn();
	private float obsolete_scale = (float) (currentTurn + 100)/(currentTurn + 40);
	
	private int OBS_DESTROYER_TURNS_scale = (int) Math.ceil(OBS_DESTROYER_TURNS * obsolete_scale);
	private int OBS_FIGHTER_TURNS_scale = (int) Math.ceil(OBS_FIGHTER_TURNS * obsolete_scale);
	private int OBS_BOMBER_TURNS_scale = (int) Math.ceil(OBS_BOMBER_TURNS * obsolete_scale);

    private final Empire empire;
    private int[] shipCounts;

    public AICShipDesigner (Empire c) {
        empire = c;
    }
    @Override
    public String toString()   { return concat("ShipDesigner: ", empire.raceName()); }
    @Override
    public Empire empire()     { return empire; }
    @Override
    public ShipDesignLab lab() { return empire.shipLab(); }
    @Override
    public void nextTurn() {
        if (empire.isAIControlled()) {
            log(this+": nextTurn");
            shipCounts = galaxy().ships.shipDesignCounts(empire.id);
            countdownObsoleteDesigns();
            // designs are updated in a specific order in order to prioritize
            // use of empty design slots
            updateFighterDesign();
            updateDestroyerDesign();
            updateBomberDesign();
            updateColonyDesign();
            updateScoutDesign();
        }
    }
    @Override
    public ShipDesign bestDesignToColonize(ShipFleet fl, StarSystem sys) {
        // find the best colony design in the fleet
        ShipDesignLab lab = lab();
        ShipDesign bestDesign = null;
        int[] colonyShips = fl.colonyShips();
        for (int i=0;i<colonyShips.length;i++) {
            if (colonyShips[i] > 0) {
                ShipDesign design = lab.design(i);
                ShipSpecialColony special = design.colonySpecial();
                if (empire.race().ignoresPlanetEnvironment()
                || (special.tech().canColonize(sys.planet())) ) {
                    if ((bestDesign == null)
                    || (design.engine().warp() > bestDesign.engine().warp())) //modnar: sign flipped from Base-AI
                        bestDesign = design;
                    else if (design.engine().warp() == bestDesign.engine().warp()) {
                        if (special.tech().environment() > bestDesign.colonySpecial().tech().environment())
                            bestDesign = design;
                    }
                }
            }
        }
        return bestDesign;
    }
    private void countdownObsoleteDesigns() {
        ShipDesignLab lab = lab();
        List<ShipDesign> scrap = new ArrayList<>();
        for (ShipDesign d: lab.designs()) {
            if (d.obsolete()) {
                d.remainingLife--;
                // if remainingLife < 0, then this design is not an
                // active design waiting to be scrapped...it's a slot that
                // needs to be freed up
                if (d.remainingLife() < 0) 
                    scrap.add(d);
            } 
        }
        for (ShipDesign d: scrap) {
            lab.scrapDesign(d);
            log("Scrapping obsolete design: "+d.name());
        }
    }
    public void updateScoutDesign() {
        ShipDesignLab lab = lab();
        
        // if we are not using scouts anymore, quit
        if (!lab.needScouts)
            return;
        
        ShipDesign currDesign = lab.scoutDesign();
        int currSlot = currDesign.id();
        if (currDesign.engine() == lab.fastestEngine())
            return;

        ShipDesign newScout = newScoutDesign();
        if (newScout.matchesDesign(currDesign))
            return;

        // NEW DESIGN IS BETTER THAN CURRENT
        // immediately replace scout design, don't bother with obsoleting them
        lab.scrapDesign(currDesign);
        lab.setScoutDesign(newScout, currSlot);
    }
    public void updateColonyDesign() {
        ShipDesignLab lab = lab();
        ShipDesign currDesign = lab.colonyDesign();
        int currSlot = currDesign.id();
        
        // weapons desirable on colony ships if we've made AI contact
        boolean weaponsWanted = !empire.contactedEmpires().isEmpty();
        ShipDesign newDesign = newColonyDesign(weaponsWanted, lab.needColonyShips, lab.needExtendedColonyShips);

        // if currDesign is obsolete, replace it immediately with new design
        if (currDesign.obsolete() && (currDesign.remainingLife() < 1)) {
            lab.scrapDesign(currDesign);
            log("Replacing obsolete colony design");
            lab.setColonyDesign(newDesign, currSlot);
            return;            
        }   

        if (newDesign.matchesDesign(currDesign))
            return;
        
        // mark existing design obsolete. this will notify the ai fleet commander
        // to not issue colonization orders until the design is replaced and allow
        // existing ships in transit to finish. This also starts a countdown timer
        // to replace it anyway
        currDesign.becomeObsolete(OBS_COLONY_TURNS);

        // do we have any colony ships in transit but not retreating.
        // if not, replace the design
        List<ShipFleet> colonyFleets = galaxy().ships.inTransitNotRetreatingFleets(empire.id, currDesign.id());
        if (colonyFleets.isEmpty()) {
            lab.scrapDesign(currDesign);
            log("No more colony ships in transit: updating colony design");
            lab.setColonyDesign(newDesign, currSlot);
            return;            
        }
    }
    public void updateBomberDesign() {
        ShipDesignLab lab = lab();
        
        // recalculate current design's damage vs. current targets
        ShipDesign currDesign = lab.bomberDesign();
        int currSlot = currDesign.id();
        
        // find best hypothetical design vs current targets
        ShipDesign newDesign = newBomberDesign(currDesign.size());

        // if currDesign is obsolete, replace it immediately
        if (currDesign.obsolete() && (currDesign.remainingLife() < 1)) {
            lab.scrapDesign(currDesign);
            log("Replacing obsolete bomber design");
            lab.setBomberDesign(newDesign, currSlot);
            return;            
        }

        if (currDesign.matchesDesign(newDesign))
            return;

        float oppShield = lab.bestEnemyPlanetaryShieldLevel();
        float bcValue = currDesign.cost()*shipCounts[currDesign.id()];
        boolean easyToReplace = bcValue <= 500; // modnar: scrap easier, change from 100
        
        if (easyToReplace) {
            newDesign.name(currDesign.name());
            if  (newDesign.size() == currDesign.size())
                newDesign.iconKey(currDesign.iconKey());
            lab.scrapDesign(currDesign);
            log("Bomber easy to replace");
            lab.setBomberDesign(newDesign, currSlot);
            return;
        }
            
        // WE HAVE BOMBERS IN USE... VERIFY THIS UPGRADE JUSTIFIES SWITCHING OVER
        boolean betterEngine = (newDesign.engine().warp() > currDesign.engine().warp());
        boolean betterArmor = (newDesign.armor().sequence() > currDesign.armor().sequence());

        float computerAdj = (newDesign.computer().level()+1) / (currDesign.computer().level()+1);
        float manvAdj = (newDesign.maneuver().level()+1) / (currDesign.maneuver().level()+1);
        float dmgRatio = newDesign.firepower(oppShield)/ currDesign.firepower(oppShield);

        // multiply various upgrade conditions... (1.05 * 1.25 * ... etc)
        // more willing to upgrade when not at war
        float upgradeThreshold = empire.atWar() ? 2 : 1.5f;
        float upgradeChance = dmgRatio * computerAdj * manvAdj;

        if (!betterEngine && !betterArmor && (upgradeChance < upgradeThreshold) )
            return;

        // mark existing design obsolete
        currDesign.becomeObsolete(OBS_BOMBER_TURNS_scale); // modnar: use scaled OBS_TURNS

        // check for an available slot for the new design
        int slot = lab.availableDesignSlot();
        
        // if there was no slot available for the new design, consider 
        // auto-upgrading anyway if we are not at war. We want to avoid
        // unnecessarily using the empty slot if we don't have to
        if ((slot < 0) && empire().enemies().isEmpty()) {
            lab.scrapDesign(currDesign);
            log("No enemies: Bomber upgrade chance:"+upgradeChance);
            lab.setBomberDesign(newDesign, currSlot);
            return;
        }
       
        // if there is a slot available, use it for the new design
        if (slot > 0) {
            log("Slot available: Bomber upgrade chance:"+upgradeChance);
            lab.setBomberDesign(newDesign, slot);
            return;
        }
    }
    public void updateFighterDesign() {
        ShipDesignLab lab = lab();
        
        // recalculate current design's damage vs. current targets
        ShipDesign currDesign = lab.fighterDesign();
        int currSlot = currDesign.id();
        ShipFighterTemplateC.setPerTurnDamage(currDesign, empire());

        // find best hypothetical design vs current targets
        ShipDesign newDesign = newFighterDesign(currDesign.size());
        
        // if currDesign is obsolete, replace it immediately with new design
        if (currDesign.obsolete() && (currDesign.remainingLife() < 1)) {
            lab.scrapDesign(currDesign);
            log("Replacing obsolete fighter design");
            lab.setFighterDesign(newDesign, currSlot);
            return;            
        }
        if (currDesign.matchesDesign(newDesign))
            return;

        // if we have very few fighters actually in use, go ahead and
        // scrap/replace now
        float bcValue = currDesign.cost()*shipCounts[currDesign.id()];
        boolean easyToReplace = bcValue <= 500; // modnar: scrap easier, change from 100
        
        if (easyToReplace) {
            newDesign.name(currDesign.name());
            if  (newDesign.size() == currDesign.size())
                newDesign.iconKey(currDesign.iconKey());
            lab.scrapDesign(currDesign);
            log("Fighter easy to replace");
            lab.setFighterDesign(newDesign, currSlot);
            return;
        }
        
        // WE HAVE FIGHTERS IN USE... VERIFY THIS UPGRADE JUSTIFIES SWITCHING OVER

        // factor in speed improvements when determining if new design is better
        // i.e. wrp 5, 100 dmg is better than wrp 3, 120 dmg
        float engineImprv = (float) (newDesign.engine().warp()+1)/ currDesign.engine().warp();
        float dmgRatio = newDesign.perTurnDamage() / currDesign.perTurnDamage();

        // switch to new design when damage is floatd
        // more willing to upgrade when not at war
        float upgradeThreshold = empire.atWar() ? 2 : 1.5f;
        float upgradeChance = dmgRatio * engineImprv;
        if (upgradeChance < upgradeThreshold)
            return;

        // mark existing design obsolete
        currDesign.becomeObsolete(OBS_FIGHTER_TURNS_scale); // modnar: use scaled OBS_TURNS

        // check for an available slot for the new design
        int slot = lab.availableDesignSlot();
        
        // if there was no slot available for the new design, consider 
        // auto-upgrading anyway if we are not at war. We want to avoid
        // unnecessarily using the empty slot if we don't have to
        if ((slot < 0) && empire().enemies().isEmpty()) {
            lab.scrapDesign(currDesign);
            log("No enemies: Fighter upgrade chance:"+upgradeChance);
            lab.setFighterDesign(newDesign, currSlot);
            return;
        }
       
        // if there is a slot available, use it for the new design
        if (slot > 0) {
            log("Slot available: Fighter upgrade chance:"+upgradeChance);
            lab.setFighterDesign(newDesign, slot);
            return;
        }
    }
    public void updateDestroyerDesign() {
        ShipDesignLab lab = lab();
        
        // recalculate current design's damage vs. current targets
        ShipDesign currDesign = lab.destroyerDesign();
        int currSlot = currDesign.id();
        ShipDestroyerTemplateC.setPerTurnDamage(currDesign, empire());

        // find best hypothetical design vs current targets
        ShipDesign newDesign = newDestroyerDesign(currDesign.size());
        
        // if currDesign is obsolete, replace it immediately with new design
        if (currDesign.obsolete() && (currDesign.remainingLife() < 1)) {
            lab.scrapDesign(currDesign);
            log("Replacing obsolete destroyer design");
            lab.setDestroyerDesign(newDesign, currSlot);
            return;            
        }

        if (currDesign.matchesDesign(newDesign))
            return;

        // if we have very few destroyers actually in use, go ahead and
        // scrap/replace now
        float bcValue = currDesign.cost()*shipCounts[currDesign.id()];
        boolean easyToReplace = bcValue <= 2000; // modnar: scrap easier, change from 1000
        
        if (easyToReplace) {
            newDesign.name(currDesign.name());
            if  (newDesign.size() == currDesign.size())
                newDesign.iconKey(currDesign.iconKey());
            lab.scrapDesign(currDesign);
            log("Destroyer easy to replace");
            lab.setDestroyerDesign(newDesign, currSlot);
            return;
        }

        // WE HAVE DESTROYERS IN USE... VERIFY THIS UPGRADE JUSTIFIES SWITCHING OVER

        // factor in speed improvements when determining if new design is better
        // i.e. wrp 5, 100 dmg is better than wrp 3, 120 dmg
        float engineImprv = (float) newDesign.engine().warp() / currDesign.engine().warp();
        float dmgRatio = newDesign.perTurnDamage() / currDesign.perTurnDamage();

        // switch to new design when damage is floatd
        float upgradeChance = dmgRatio * engineImprv;
        float upgradeThreshold = currDesign.buildCount() == 0 ? 1.1f : 2.0f;
        if (upgradeChance <= upgradeThreshold)
            return;
     
        // mark existing design obsolete
        currDesign.becomeObsolete(OBS_DESTROYER_TURNS_scale); // modnar: use scaled OBS_TURNS

        // check for an available slot for the new design
        int slot = lab.availableDesignSlot();
        
        // if there was no slot available for the new design, consider 
        // auto-upgrading anyway if we are not at war. We want to avoid
        // unnecessarily using the empty slot if we don't have to
        if ((slot < 0) && empire().enemies().isEmpty()) {
            lab.scrapDesign(currDesign);
            log("No enemies: Destroyer upgrade chance:"+upgradeChance);
            lab.setDestroyerDesign(newDesign, currSlot);
            return;
        }
       
        // if there is a slot available, use it for the new design
        if (slot > 0) {
            log("Slot available: Destroyer upgrade chance:"+upgradeChance);
            lab.setDestroyerDesign(newDesign, slot);
            return;
        }
    }
    @Override
    public ShipDesign newScoutDesign() {
        ShipDesignLab lab = lab();
        ShipDesign design = lab.newBlankDesign(ShipDesign.SMALL);
        design.engine(lab.fastestEngine());
        ShipSpecial special = lab.specialReserveFuel();
        design.special(0, special);
        design.setSmallestSize();
        design.mission(ShipDesign.SCOUT);
        design.maxUnusedTurns(OBS_SCOUT_TURNS);
        lab.nameDesign(design);
        lab.iconifyDesign(design);
        return design;
    }
    public ShipDesign newColonyDesign(boolean weaponDesired, boolean regularRangeNeeded, boolean extendedRangeNeeded) {
        ShipDesignLab lab = lab();
        ShipDesign design = lab.newBlankDesign(ShipDesign.LARGE);
        design.special(0, bestColonySpecial());
        design.engine(lab.fastestEngine());
        design.mission(ShipDesign.COLONY);
        design.maxUnusedTurns(OBS_COLONY_TURNS);
        design.setSmallestSize();
        int standardSize = design.size();

        // Try to fit extended range it
        if (extendedRangeNeeded) {
            ShipSpecial extRangeSpecial = lab.specialReserveFuel();
            design.special(1, extRangeSpecial);
            design.setSmallestSize();
            // Cancel that if we made the ship bigger, unless the only candidate planets require it
            boolean desperateForRange = !regularRangeNeeded && extendedRangeNeeded;
            if (design.size() > standardSize && !desperateForRange) 
                design.special(1, lab.noSpecial());            	
        }

        // AIs will always put 1 beam weapon on their colony ships if AI contact made
        if (weaponDesired) {
            ShipWeapon bestWpn = lab.bestUnlimitedShotWeapon(design, 1);
            if (bestWpn != null) 
                design.addWeapon(bestWpn, 1);
        }
        
        design.setSmallestSize();
        lab.nameDesign(design);
        lab.iconifyDesign(design);
        return design;
    }
    @Override
    public int optimalShipFighterSize() {
        int preferredSize = empire.race().preferredShipSize();
        if (preferredSize == ShipDesign.SMALL)
            return preferredSize;

        List<StarSystem> systems = empire.allColonizedSystems();
        float maxProd = 0;
        for (StarSystem sys: systems)
            maxProd = max(sys.colony().production(), maxProd);

        int maxSize = ShipDesign.SMALL;
        if (maxProd >= 1500)
            maxSize = ShipDesign.HUGE;
        else if (maxProd >= 700) // modnar: change from 300, keep fighters smaller
            maxSize = ShipDesign.LARGE;
        else if (maxProd >= 300) // modnar: change from 60 (!), keep fighters smaller
            maxSize = ShipDesign.MEDIUM;
        return min(preferredSize, maxSize);
    }
    @Override
    public int optimalShipBomberSize() {
        int preferredSize = empire.race().preferredShipSize()+1;
        if (preferredSize == ShipDesign.LARGE)
            return ShipDesign.LARGE;

        List<StarSystem> systems = empire.allColonizedSystems();
        float maxProd = 0;
        for (StarSystem sys: systems)
            maxProd = max(sys.colony().production(), maxProd);

        int maxSize = ShipDesign.MEDIUM;
        if (maxProd >= 1500) // modnar: change from 1000, keep pure bombers smaller
            maxSize = ShipDesign.HUGE;
        else if (maxProd >= 450) // modnar: change from 200, keep pure bombers smaller
            maxSize = ShipDesign.LARGE;
        return min(preferredSize, maxSize);
    }
    @Override
    public int optimalShipDestroyerSize() {
        int preferredSize = empire.race().preferredShipSize()+1;
        if (preferredSize == ShipDesign.LARGE)
            return ShipDesign.LARGE;

        List<StarSystem> systems = empire.allColonizedSystems();
        float maxProd = 0;
        for (StarSystem sys: systems)
            maxProd = max(sys.colony().production(), maxProd);

        int maxSize = ShipDesign.MEDIUM;
        if (maxProd >= 800) // modnar: change from 1000, pump out HUGE destroyers sooner, 800 is around soil/terraform+40/robo-4
            maxSize = ShipDesign.HUGE;
        else if (maxProd >= 350) // modnar: change from 200, keep destroyer smaller in beginning
            maxSize = ShipDesign.LARGE;
        return min(preferredSize, maxSize);
    }
    @Override
    public ShipDesign newFighterDesign(int size) {
        ShipDesign design = ShipFighterTemplateC.newDesign(this);
        design.mission(ShipDesign.FIGHTER);
        design.maxUnusedTurns(OBS_FIGHTER_TURNS_scale); // modnar: use scaled OBS_TURNS
        return design;
    }
    @Override
    public ShipDesign newBomberDesign(int size) {
        ShipDesign design = ShipBomberTemplateC.newDesign(this);
        design.mission(ShipDesign.BOMBER);
        design.maxUnusedTurns(OBS_BOMBER_TURNS_scale); // modnar: use scaled OBS_TURNS
        return design;
    }
    @Override
    public ShipDesign newDestroyerDesign(int size) {
        ShipDesign design = ShipDestroyerTemplateC.newDesign(this);
        design.mission(ShipDesign.DESTROYER);
        design.maxUnusedTurns(OBS_DESTROYER_TURNS_scale); // modnar: use scaled OBS_TURNS
        return design;
    }
    @Override
    public ShipSpecialColony bestColonySpecial() {
        ShipSpecialColony bestSpecial = null;
        boolean ignoreEnv = empire.race().ignoresPlanetEnvironment();

        for (ShipSpecial spec : empire.shipLab().specials()) {
            if (spec.isColonySpecial()) {
                ShipSpecialColony colSpec = (ShipSpecialColony) spec;
                if (bestSpecial == null)
                    bestSpecial = colSpec;
                else if (ignoreEnv) {
                    if (bestSpecial.cost() >  colSpec.cost())  // silicoids measure by cost
                        bestSpecial = colSpec;
                }
                else {
                    if (bestSpecial.tech().hostilityAllowed() < colSpec.tech().hostilityAllowed())
                        bestSpecial = colSpec;
                }
            }
        }
        return bestSpecial;
    }
}
