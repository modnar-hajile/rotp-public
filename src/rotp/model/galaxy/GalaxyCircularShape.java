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
package rotp.model.galaxy;

import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import rotp.model.game.IGameOptions;

public class GalaxyCircularShape extends GalaxyShape {
    private static final long serialVersionUID = 1L;
    Shape circle;
	Area totalArea, circleArea;
	
    public GalaxyCircularShape(IGameOptions options) {
        opts = options;
    }
    @Override
    public void init(int n) {
        super.init(n);
		
		float gE = (float) galaxyEdgeBuffer();
		float gW = (float) galaxyWidthLY();
		float gH = (float) galaxyHeightLY();
		
		// modnar: different circle configurations with setMapOption
		if (opts.setMapOption() == 1) {
			// single circle
			// modnar: add galaxyEdgeBuffer() as upper left corner to prevent cutoff
			circle = new Ellipse2D.Float(gE,gE,gW,gH);
			circleArea = new Area(circle);
			totalArea = circleArea;
		}
		else if (opts.setMapOption() == 2) {
			// close-packing 3 circles (triangle)
			circle = new Ellipse2D.Float(gE+0.25f*gW, gE+0.03f*gH, 0.5f*gW, 0.5f*gH);
			circleArea = new Area(circle);
			totalArea = circleArea;
			
			circle = new Ellipse2D.Float(gE+0.0f*gW, gE+0.465f*gH, 0.5f*gW, 0.5f*gH);
			circleArea = new Area(circle);
			totalArea.add(circleArea);
			
			circle = new Ellipse2D.Float(gE+0.5f*gW, gE+0.465f*gH, 0.5f*gW, 0.5f*gH);
			circleArea = new Area(circle);
			totalArea.add(circleArea);
		}
		else if (opts.setMapOption() == 3) {
			// 4 circles (square)
			circle = new Ellipse2D.Float(gE+0.0f*gW, gE+0.0f*gH, 0.5f*gW, 0.5f*gH);
			circleArea = new Area(circle);
			totalArea = circleArea;
			
			circle = new Ellipse2D.Float(gE+0.5f*gW, gE+0.0f*gH, 0.5f*gW, 0.5f*gH);
			circleArea = new Area(circle);
			totalArea.add(circleArea);
			
			circle = new Ellipse2D.Float(gE+0.0f*gW, gE+0.5f*gH, 0.5f*gW, 0.5f*gH);
			circleArea = new Area(circle);
			totalArea.add(circleArea);
			
			circle = new Ellipse2D.Float(gE+0.5f*gW, gE+0.5f*gH, 0.5f*gW, 0.5f*gH);
			circleArea = new Area(circle);
			totalArea.add(circleArea);
		}
    }
    @Override
    public float maxScaleAdj()               { return 1.1f; }
    @Override
    protected int galaxyWidthLY() { 
        return (int) (Math.sqrt(maxStars*adjustedSizeFactor()));
    }
    @Override
    protected int galaxyHeightLY() { 
        return (int) (Math.sqrt(maxStars*adjustedSizeFactor()));
    }
    @Override
    public void setRandom(Point.Float pt) {
        pt.x = randomLocation(width, galaxyEdgeBuffer());
        pt.y = randomLocation(height, galaxyEdgeBuffer());
    }
    @Override
    public boolean valid(Point.Float pt) {
        return totalArea.contains(pt.x, pt.y);
    }
    float randomLocation(float max, float buff) {
        return buff + (random() * (max-buff-buff));
    }
    @Override
    protected float sizeFactor(String size) {
        switch (opts.selectedGalaxySize()) {
            case IGameOptions.SIZE_TINY:      return 12; 
            case IGameOptions.SIZE_SMALL:     return 12; 
            case IGameOptions.SIZE_SMALL2:    return 13;
            case IGameOptions.SIZE_MEDIUM:    return 13; 
            case IGameOptions.SIZE_MEDIUM2:   return 14; 
            case IGameOptions.SIZE_LARGE:     return 16; 
            case IGameOptions.SIZE_LARGE2:    return 18; 
            case IGameOptions.SIZE_HUGE:      return 20; 
            case IGameOptions.SIZE_HUGE2:     return 22; 
            case IGameOptions.SIZE_MASSIVE:   return 24; 
            case IGameOptions.SIZE_MASSIVE2:  return 26; 
            case IGameOptions.SIZE_MASSIVE3:  return 28; 
            case IGameOptions.SIZE_MASSIVE4:  return 30; 
            case IGameOptions.SIZE_MASSIVE5:  return 32; 
            case IGameOptions.SIZE_INSANE:    return 36; 
            case IGameOptions.SIZE_LUDICROUS: return 40; 
            default:             return 19; 
        }
    }
}
