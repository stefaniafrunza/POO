import java.util.*;

public class LoyalityVoucher extends Voucher {
	float discount;

	public LoyalityVoucher(int id, String email, int id_cmpgn, float discount) {
		this.id = id;
		this.code = UUID.randomUUID().toString();
		this.email = email;
		this.id_cmpgn = id_cmpgn;
		this.date = null;
		this.vst = Voucher.VoucherStatusType.UNUSED;
		this.discount = discount;
	}

	public String toString() {
		return "[" + this.id + ";" + this.vst + ";" + this.email + ";" + this.discount
			+ ";" + this.id_cmpgn + "" + this.date + "]";
	}
}