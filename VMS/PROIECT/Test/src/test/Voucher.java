/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;
import java.time.*;

public abstract class Voucher {
    int id;
    String code;
    enum VoucherStatusType {
        USED, UNUSED
    };
    VoucherStatusType vst;
    LocalDateTime date;
    String email;
    int id_cmpgn;
}