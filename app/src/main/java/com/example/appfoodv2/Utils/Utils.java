package com.example.appfoodv2.Utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class Utils {
    public static String unsignedString(String name) {
        String nfdNormalizedString = Normalizer.normalize(name, Normalizer.Form.NFD);
        nfdNormalizedString = nfdNormalizedString.replace("đ", "d");
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }
}
