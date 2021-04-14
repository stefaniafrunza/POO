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