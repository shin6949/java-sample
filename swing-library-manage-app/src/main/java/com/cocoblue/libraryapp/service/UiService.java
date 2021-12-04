package com.cocoblue.libraryapp.service;

import javax.swing.*;

public class UiService {
    public void processProgressbar(JProgressBar progressBar, int value) {
        switch (value) {
            case 0:
                if (progressBar != null) {
                    progressBar.setVisible(true);
                    progressBar.setValue(0);
                }
                break;

            case 100:
                if (progressBar != null) {
                    progressBar.setValue(100);
                    progressBar.setVisible(false);
                }
                break;

            default:
                if (progressBar != null) {
                    progressBar.setValue(value);
                }
                break;
        }
    }

    public String convert_status(Boolean status) {
        String string_status = null;

        if (status) {
            string_status = "���� ����";
        } else {
            string_status = "���� �Ұ�";
        }

        return string_status;
    }
}
