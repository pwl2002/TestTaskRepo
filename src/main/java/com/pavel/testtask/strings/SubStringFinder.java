/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pavel.testtask.strings;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author PM
 */

public class SubStringFinder {

    public StringBuffer findSubString(String str) {

        Set<Character> usedChar = new HashSet();
        StringBuffer res = new StringBuffer(usedChar.size());
        
        for (int i = 0; i < str.length(); i++) {
            usedChar.add(str.charAt(i));
        }

        for (int i = 0; i < str.length(); i++) {
            if (res.indexOf(String.valueOf(str.charAt(i))) == -1) {
                res.append(str.charAt(i));
            } else if (res.length() == usedChar.size()) {
                return res;
            } else {
                res.delete(0, res.indexOf(String.valueOf(str.charAt(i))) + 1);
                res.append(str.charAt(i));
            }
        }
        return res;
    }
}
