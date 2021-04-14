/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.*;

public class CampaignVoucherMap extends ArrayMap<String, LinkedList<Voucher>> {
    public boolean addVoucher(Voucher v) {
        for(String i : this.keySet()) {
            if(this.get(i).contains(v) == true)
                return false;
        }

        int ok = 0;
        for(String i : this.keySet()) {
            if(i == v.email)
                ok = 1;
        }

        if(ok == 0)
            return false;

        this.get(v.email).add(v);
        return true;
    }
}