/*
 * NMRFx: A Program for Processing NMR Data
 * Copyright (C) 2004-2022 One Moon Scientific, Inc., Westfield, N.J., USA
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.nmrfx.jmx.mbeans;

import javafx.stage.Stage;
import org.nmrfx.jmx.NotificationType;
import org.nmrfx.jmx.mbeans.AnalystMBean;
import org.nmrfx.processor.gui.FXMLController;
import org.nmrfx.processor.gui.controls.ConsoleUtil;

import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;

/**
 * The main entrypoint to control NMRFx Analyst Gui by JMX.
 */
public class Analyst extends NotificationBroadcasterSupport implements AnalystMBean {
    private int notificationSequenceNumber = 0;

    @Override
    public void open(String path) {
        ConsoleUtil.runOnFxThread(() -> {
            FXMLController.getActiveController().openFile(path, false, true);
            sendNotification(NotificationType.MESSAGE, "File opened", path);
        });
    }

    @Override
    public void setWindowOnFront() {
        ConsoleUtil.runOnFxThread(() -> {
            Stage stage = FXMLController.getActiveController().getStage();
            stage.toFront();
            stage.requestFocus();
        });
    }

    protected void sendNotification(NotificationType type, String message, Object userData) {
        var notification = new Notification(type.name(), this, notificationSequenceNumber++, System.currentTimeMillis(), message);
        notification.setUserData(userData);
        sendNotification(notification);
    }
}