import java.util.*;

public class GiftVoucher extends Voucher {
	float sum;

	public GiftVoucher(int id, String email, int id_cmpgn, float sum) {
		this.id = id;
		this.code = UUID.randomUUID().toString();
		this.email = email;
		this.id_cmpgn = id_cmpgn;
		this.date = null;
		this.vst = Voucher.VoucherStatusType.UNUSED;
		this.sum = sum;
	}

	public String toString() {
		String result =  "[" + this.id + ";" + this.vst + ";" + this.email + ";" + this.sum
			+ ";" + this.id_cmpgn + ";" + this.date + "]";
		return result;
	}
}