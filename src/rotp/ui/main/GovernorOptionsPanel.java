package rotp.ui.main;

import java.awt.event.MouseWheelEvent;
import javax.swing.JFrame;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import rotp.model.galaxy.StarSystem;
import rotp.model.game.GameSession;
import rotp.model.game.GovernorOptions;
import rotp.model.game.GovernorOptions2;
import rotp.ui.RotPUI;

/**
 * Produced using Netbeans Swing GUI builder.
 */
public class GovernorOptionsPanel extends javax.swing.JPanel {
    private final JFrame frame;
    public GovernorOptionsPanel(JFrame frame) {
        this.frame = frame;
        initComponents();
        // initial values
        GovernorOptions options = GameSession.instance().getGovernorOptions();
        GovernorOptions2 options2 = GameSession.instance().getGovernorOptions2();
        this.governorDefault.setSelected(options.isGovernorOnByDefault());
        this.autotransport.setSelected(options.isAutotransport());
        this.transportPop.setValue(options2.getTransportPopulation());
        this.transportMaxPercent.setValue(options2.getTransportMaxPercent());
        this.transportMaxTurns.setValue(options2.getTransportMaxTurns());
        changePopulationLabel();
        switch (GameSession.instance().getGovernorOptions().getGates()) {
            case None:
                this.stargateOff.setSelected(true);
                break;
            case Rich:
                this.stargateRich.setSelected(true);
                break;
            case All:
                this.stargateOn.setSelected(true);
                break;
        }
        this.missileBases.setValue(options2.getMinimumMissileBases());
        this.autospend.setSelected(options2.isAutospend());
        this.reserve.setValue(options2.getReserve());
        this.shipbuilding.setSelected(options2.isShipbuilding());
        this.autoScout.setSelected(options2.isAutoScout());
        this.autoColonize.setSelected(options2.isAutoColonize());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        stargateOptions = new javax.swing.ButtonGroup();
        governorDefault = new javax.swing.JCheckBox();
        javax.swing.JPanel autotransportPanel = new javax.swing.JPanel();
        autotransport = new javax.swing.JCheckBox();
        transportPop = new javax.swing.JSpinner();
        javax.swing.JLabel transportPopLabel = new javax.swing.JLabel();
        transportMaxPercent = new javax.swing.JSpinner();
        javax.swing.JLabel transportMaxPercentLabel = new javax.swing.JLabel();
        transportSizeLabel = new javax.swing.JLabel();
        transportMaxTurns = new javax.swing.JSpinner();
        javax.swing.JLabel transportMaxTurnsLabel = new javax.swing.JLabel();
        javax.swing.JLabel transportMaxTurnsNebula = new javax.swing.JLabel();
        allGovernorsOn = new javax.swing.JButton();
        allGovernorsOff = new javax.swing.JButton();
        javax.swing.JPanel stargatePanel = new javax.swing.JPanel();
        stargateOff = new javax.swing.JRadioButton();
        stargateRich = new javax.swing.JRadioButton();
        stargateOn = new javax.swing.JRadioButton();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        missileBases = new javax.swing.JSpinner();
        missileBasesLabel = new javax.swing.JLabel();
        autospend = new javax.swing.JCheckBox();
        reserve = new javax.swing.JSpinner();
        resrveLabel = new javax.swing.JLabel();
        shipbuilding = new javax.swing.JCheckBox();
        autoScout = new javax.swing.JCheckBox();
        autoColonize = new javax.swing.JCheckBox();

        governorDefault.setText("Governor is on by default");

        autotransportPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Autotransport Options"));

        autotransport.setText("Population automatically transported from full colonies");

        transportPop.setModel(new javax.swing.SpinnerNumberModel(10, 1, 10, 1));
        transportPop.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                transportPopStateChanged(evt);
            }
        });
        transportPop.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                transportPopMouseWheelMoved(evt);
            }
        });

        transportPopLabel.setText("Population to transport");
        transportPopLabel.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                transportPopLabelMouseWheelMoved(evt);
            }
        });

        transportMaxPercent.setModel(new javax.swing.SpinnerNumberModel(20, 3, 20, 1));
        transportMaxPercent.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                transportMaxPercentStateChanged(evt);
            }
        });
        transportMaxPercent.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                transportMaxPercentMouseWheelMoved(evt);
            }
        });

        transportMaxPercentLabel.setText("Maximum population % to transport");
        transportMaxPercentLabel.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                transportMaxPercentLabelMouseWheelMoved(evt);
            }
        });

        transportSizeLabel.setText("Only planets size X and above will transport population");

        transportMaxTurns.setModel(new javax.swing.SpinnerNumberModel(15, 1, 15, 1));
        transportMaxTurns.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                transportMaxTurnsMouseWheelMoved(evt);
            }
        });

        transportMaxTurnsLabel.setText("Maximum transport distance in turns");
        transportMaxTurnsLabel.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                transportMaxTurnsLabelMouseWheelMoved(evt);
            }
        });

        transportMaxTurnsNebula.setText("(1.5x higher distance when transporting to nebulae)");

        javax.swing.GroupLayout autotransportPanelLayout = new javax.swing.GroupLayout(autotransportPanel);
        autotransportPanel.setLayout(autotransportPanelLayout);
        autotransportPanelLayout.setHorizontalGroup(
            autotransportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(autotransportPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(autotransportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(autotransport)
                    .addGroup(autotransportPanelLayout.createSequentialGroup()
                        .addGroup(autotransportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(transportPop)
                            .addComponent(transportMaxPercent))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(autotransportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(transportPopLabel)
                            .addComponent(transportMaxPercentLabel)))
                    .addComponent(transportSizeLabel)
                    .addGroup(autotransportPanelLayout.createSequentialGroup()
                        .addComponent(transportMaxTurns, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(transportMaxTurnsLabel))
                    .addComponent(transportMaxTurnsNebula))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        autotransportPanelLayout.setVerticalGroup(
            autotransportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(autotransportPanelLayout.createSequentialGroup()
                .addComponent(autotransport)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(autotransportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(transportPop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(transportPopLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(autotransportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(transportMaxPercent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(transportMaxPercentLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(transportSizeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(autotransportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(transportMaxTurns, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(transportMaxTurnsLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(transportMaxTurnsNebula))
        );

        allGovernorsOn.setText("All Governors ON");
        allGovernorsOn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allGovernorsOnActionPerformed(evt);
            }
        });

        allGovernorsOff.setText("All Governors OFF");
        allGovernorsOff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allGovernorsOffActionPerformed(evt);
            }
        });

        stargatePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Stargate Options"));

        stargateOptions.add(stargateOff);
        stargateOff.setText("Never build stargates");

        stargateOptions.add(stargateRich);
        stargateRich.setText("Build stargates on Rich and Ultra Rich planets");

        stargateOptions.add(stargateOn);
        stargateOn.setText("Always build stargates");

        javax.swing.GroupLayout stargatePanelLayout = new javax.swing.GroupLayout(stargatePanel);
        stargatePanel.setLayout(stargatePanelLayout);
        stargatePanelLayout.setHorizontalGroup(
            stargatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(stargatePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(stargatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(stargateOff)
                    .addComponent(stargateRich)
                    .addComponent(stargateOn))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        stargatePanelLayout.setVerticalGroup(
            stargatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(stargatePanelLayout.createSequentialGroup()
                .addComponent(stargateOff)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(stargateRich)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(stargateOn))
        );

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        missileBases.setModel(new javax.swing.SpinnerNumberModel(0, 0, 20, 1));
        missileBases.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                missileBasesMouseWheelMoved(evt);
            }
        });

        missileBasesLabel.setText("Minimum missile bases");

        autospend.setText("Autospend");
        autospend.setToolTipText("Automatically spend reserve on planets with lowest production");

        reserve.setModel(new javax.swing.SpinnerNumberModel(1000, 0, 100000, 10));
        reserve.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                reserveMouseWheelMoved(evt);
            }
        });

        resrveLabel.setText("Keep in reserve");

        shipbuilding.setText("Shipbuilding with Governor enabled");
        shipbuilding.setToolTipText("Divert resources into shipbuilding and not research if planet is already building ships");

        autoScout.setText("Auto Scout");
        autoScout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoScoutActionPerformed(evt);
            }
        });

        autoColonize.setText("Auto Colonize");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(autotransportPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(stargatePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(allGovernorsOn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(allGovernorsOff))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 265, Short.MAX_VALUE)
                        .addComponent(autoColonize)
                        .addGap(108, 108, 108))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(missileBases, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(missileBasesLabel))
                            .addComponent(shipbuilding)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(autospend)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(reserve, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(resrveLabel))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(governorDefault)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(autoScout)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(okButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cancelButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(governorDefault)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(autotransportPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(allGovernorsOn)
                    .addComponent(allGovernorsOff))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stargatePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(autoScout)
                    .addComponent(autoColonize))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(missileBases, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(missileBasesLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(autospend)
                    .addComponent(reserve, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(resrveLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(shipbuilding)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton)
                    .addComponent(cancelButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void allGovernorsOnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allGovernorsOnActionPerformed
        for (StarSystem ss : GameSession.instance().galaxy().player().orderedColonies()) {
            if (!ss.isColonized()) {
                // shouldn't happen
                continue;
            }
            ss.colony().setGovernor(true);
            ss.colony().governIfNeeded();
        }
    }//GEN-LAST:event_allGovernorsOnActionPerformed

    private void transportPopMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_transportPopMouseWheelMoved
        mouseWheel(transportPop, evt);
    }//GEN-LAST:event_transportPopMouseWheelMoved

    private void transportMaxPercentMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_transportMaxPercentMouseWheelMoved
        mouseWheel(transportMaxPercent, evt);
    }//GEN-LAST:event_transportMaxPercentMouseWheelMoved

    private void transportMaxTurnsMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_transportMaxTurnsMouseWheelMoved
        mouseWheel(transportMaxTurns, evt);
    }//GEN-LAST:event_transportMaxTurnsMouseWheelMoved

    private void transportPopLabelMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_transportPopLabelMouseWheelMoved
        mouseWheel(transportPop, evt);
    }//GEN-LAST:event_transportPopLabelMouseWheelMoved

    private void transportMaxPercentLabelMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_transportMaxPercentLabelMouseWheelMoved
        mouseWheel(transportMaxPercent, evt);
    }//GEN-LAST:event_transportMaxPercentLabelMouseWheelMoved

    private void transportMaxTurnsLabelMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_transportMaxTurnsLabelMouseWheelMoved
        mouseWheel(transportMaxTurns, evt);
    }//GEN-LAST:event_transportMaxTurnsLabelMouseWheelMoved

    private void allGovernorsOffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allGovernorsOffActionPerformed
        for (StarSystem ss : GameSession.instance().galaxy().player().orderedColonies()) {
            if (!ss.isColonized()) {
                // shouldn't happen
                continue;
            }
            ss.colony().setGovernor(false);
        }
    }//GEN-LAST:event_allGovernorsOffActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        GovernorOptions options = GameSession.instance().getGovernorOptions();
        GovernorOptions2 options2 = GameSession.instance().getGovernorOptions2();
        options.setGovernorOnByDefault(governorDefault.isSelected());
        options.setAutotransport(autotransport.isSelected());
        options2.setTransportPopulation((Integer)transportPop.getValue());
        options2.setTransportMaxPercent((Integer)transportMaxPercent.getValue());
        options2.setTransportMaxTurns((Integer)transportMaxTurns.getValue());

        if (stargateOff.isSelected()) {
            options.setGates(GovernorOptions.GatesGovernor.None);
        } else if (stargateRich.isSelected()) {
            options.setGates(GovernorOptions.GatesGovernor.Rich);
        } else if (stargateOn.isSelected()) {
            options.setGates(GovernorOptions.GatesGovernor.All);
        }
        options2.setMinimumMissileBases((Integer)missileBases.getValue());
        options2.setAutospend(autospend.isSelected());
        options2.setReserve((Integer)reserve.getValue());
        options2.setShipbuilding(shipbuilding.isSelected());
        options2.setAutoScout(autoScout.isSelected());
        options2.setAutoColonize(autoColonize.isSelected());
        frame.setVisible(false);
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        frame.setVisible(false);
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void transportPopStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_transportPopStateChanged
        changePopulationLabel();
    }//GEN-LAST:event_transportPopStateChanged

    private void transportMaxPercentStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_transportMaxPercentStateChanged
        changePopulationLabel();
    }//GEN-LAST:event_transportMaxPercentStateChanged

    private void missileBasesMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_missileBasesMouseWheelMoved
        mouseWheel(missileBases, evt);
    }//GEN-LAST:event_missileBasesMouseWheelMoved

    private void reserveMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_reserveMouseWheelMoved
        mouseWheel(reserve, evt);
    }//GEN-LAST:event_reserveMouseWheelMoved

    private void autoScoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoScoutActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_autoScoutActionPerformed

    private void changePopulationLabel() {
        int pop = (int)this.transportPop.getValue();
        int maxPercent = (int)this.transportMaxPercent.getValue();
        int size = pop * 100 / maxPercent;
        String msg = String.format("Only planets size %d and above will transport population", size);
        transportSizeLabel.setText(msg);
    }
    private static void mouseWheel(JSpinner spinner, java.awt.event.MouseWheelEvent evt) {
        if (evt.getScrollType() != MouseWheelEvent.WHEEL_UNIT_SCROLL) {
            return;
        }
        SpinnerNumberModel model = (SpinnerNumberModel) spinner.getModel();
        int value = (int) model.getValue();
        // always scroll integers by 1
        value -= Math.signum(evt.getUnitsToScroll()) * model.getStepSize().intValue();
        int minimum = ((Number)model.getMinimum()).intValue();
        int maximum = ((Number)model.getMaximum()).intValue();
        if (value < minimum) {
            value = minimum;
        }
        if (value > maximum) {
            value = maximum;
        }
        spinner.setValue(value);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton allGovernorsOff;
    private javax.swing.JButton allGovernorsOn;
    private javax.swing.JCheckBox autoColonize;
    private javax.swing.JCheckBox autoScout;
    private javax.swing.JCheckBox autospend;
    private javax.swing.JCheckBox autotransport;
    private javax.swing.JButton cancelButton;
    private javax.swing.JCheckBox governorDefault;
    private javax.swing.JSpinner missileBases;
    private javax.swing.JLabel missileBasesLabel;
    private javax.swing.JButton okButton;
    private javax.swing.JSpinner reserve;
    private javax.swing.JLabel resrveLabel;
    private javax.swing.JCheckBox shipbuilding;
    private javax.swing.JRadioButton stargateOff;
    private javax.swing.JRadioButton stargateOn;
    private javax.swing.ButtonGroup stargateOptions;
    private javax.swing.JRadioButton stargateRich;
    private javax.swing.JSpinner transportMaxPercent;
    private javax.swing.JSpinner transportMaxTurns;
    private javax.swing.JSpinner transportPop;
    private javax.swing.JLabel transportSizeLabel;
    // End of variables declaration//GEN-END:variables

    // Just test the layout
    public static void main(String arg[]) {
        // initialize everything
        RotPUI.instance();

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("GovernorOptions");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                //Create and set up the content pane.
                GovernorOptionsPanel newContentPane = new GovernorOptionsPanel(frame);
                newContentPane.setOpaque(true); //content panes must be opaque
                frame.setContentPane(newContentPane);

                //Display the window.
                frame.pack();
                frame.setVisible(true);
            }
        });

    }
}
