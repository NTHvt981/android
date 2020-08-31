package com.example.giaodientest.DTO;

import java.text.DecimalFormat;

public class ZUltility {
    static String formatMoney(String number)
    {
        double amount = Double.parseDouble(number);
        DecimalFormat formatter = new DecimalFormat("#,###");

        return formatter.format(amount);
    }
}
