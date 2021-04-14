import java.util.*;
import java.io.*;
import java.time.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Campaign {
	int id;
	String name;
	String description;
	LocalDateTime startDate;
	LocalDateTime endDate;
	int nrVouchers;
	int nrCurrAvVouchers;
	enum CampaignStatusType {
		NEW, STARTED, EXPIRED, CANCELLED
	};
	CampaignStatusType cst;
	CampaignVoucherMap vouchers;
	LinkedList<User> observers;
	String strategy;
	AtomicInteger count = new AtomicInteger(1);
	VMS vms = VMS.getInstance();

	public Campaign(int id, String name, String description, LocalDateTime startDate,
		LocalDateTime endDate, int nrVouchers, String strategy, LocalDateTime currDate) {

		this.id = id;
		this.name = name;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.nrVouchers = nrVouchers;
		this.nrCurrAvVouchers = nrVouchers;

		if(currDate.isBefore(startDate) == true)
			this.cst = Campaign.CampaignStatusType.NEW;
		else {
			if(currDate.isAfter(endDate) == true)
				this.cst = Campaign.CampaignStatusType.EXPIRED;
			else
				this.cst = Campaign.CampaignStatusType.STARTED;
		}

		this.vouchers = new CampaignVoucherMap();
		this.observers = new LinkedList<User>();
		this.strategy = strategy;
	}

	public String toString() {
		String result = this.id + " " + this.name + " " + this.description + " " + this.cst +
			" " + this.nrCurrAvVouchers + " " + this.strategy + " " + this.startDate + " " +
			this.endDate + " " + this.vouchers;

		return result;
	}

	public LinkedList<Voucher> getVouchers() {
		LinkedList<Voucher> campaignVouchers = new LinkedList<Voucher>();
		for(String email : vouchers.keySet()) {
			LinkedList<Voucher> vouc = vouchers.get(email);
			for(int j = 0; j < vouc.size(); j++) {
				campaignVouchers.add(vouc.get(j));
			}
		}
		return campaignVouchers;
	}

	public Voucher getVoucher(String code) {
		for(String email : vouchers.keySet()) {
			LinkedList<Voucher> vouc = vouchers.get(email);
			for(int j = 0; j < vouc.size(); j++) {
				if((vouc.get(j).code).equals(code))
					return vouc.get(j);
			}
		}
		return null;
	}

	public LinkedList<User> getObservers() {
		return this.observers;
	}

	public void addObserver(User user) {
		this.observers.add(user);
	}

	public void removeObserver(User user) {
		this.observers.remove(user);
	}

	public void notifyAllObservers(Notification notification) {
		for(int i = 0; i < this.observers.size(); i++) {
			observers.get(i).update(notification);
		}
	}

	public Voucher generateVoucher(String email, String voucherType, float value) {
		if(this.cst.equals(CampaignStatusType.EXPIRED) ||
			this.cst.equals(CampaignStatusType.CANCELLED)) {

			return null;
		}

		if(this.nrCurrAvVouchers == 0) {
			return null;
		}

		int bec = 0;
		for(User u : vms.users) 
			if(u.email.equals(email))
				bec = 1;

		if(bec == 0)
			return null;

		Voucher vouc = null;

		if(voucherType.equals("GiftVoucher")) {
			vouc = new GiftVoucher(this.count.getAndIncrement(), email, this.id, value);
			for(User u : vms.users) {
				if(u.email.equals(email)) {
					if(u.vouchers.containsKey(this.id) == false) {
						addObserver(u);
						LinkedList<Voucher> listVouc = new LinkedList<Voucher>();
						listVouc.add(vouc);
						u.vouchers.put(this.id, listVouc);
						this.vouchers.put(email, listVouc);
					}
					else {
						u.vouchers.addVoucher(vouc);
						vouchers.addVoucher(vouc);
					}
				}
			}
		}
			
		if(voucherType.equals("LoyaltyVoucher")) {
			vouc = new LoyalityVoucher(this.count.getAndIncrement(), email, this.id, value);
			for(User u : vms.users) {
				if(u.email.equals(email)) {
					if(u.vouchers.containsKey(this.id) == false) {
						addObserver(u);
						LinkedList<Voucher> listVouc = new LinkedList<Voucher>();
						listVouc.add(vouc);
						u.vouchers.put(this.id, listVouc);
						this.vouchers.put(email, listVouc);
					}
					else {
						u.vouchers.addVoucher(vouc);
						vouchers.addVoucher(vouc);
					}
				}
			}
		}

		this.nrCurrAvVouchers--;
		return vouc;
	}

	public void redeemVoucher(int id, LocalDateTime currDate) {
		Voucher vouc;
		vouc = null;

		for(User u : observers) {
			LinkedList<Voucher> usersCampaignVouc = u.vouchers.get(this.id);
			for(int j = 0; j < usersCampaignVouc.size(); j++) {
				if(usersCampaignVouc.get(j).id == id) {
					vouc = usersCampaignVouc.get(j);
				}
			}
		}

		if(vouc == null)
			return;

		if(vouc.vst.equals(Voucher.VoucherStatusType.USED) ||
			currDate.isBefore(this.startDate) == true ||
			currDate.isAfter(this.endDate) == true ||
			this.cst.equals(Campaign.CampaignStatusType.EXPIRED) ||
			this.cst.equals(Campaign.CampaignStatusType.CANCELLED))

			return;

		vouc.vst = Voucher.VoucherStatusType.USED;
		vouc.date = currDate;
	}

	public Voucher executeStrategy() {
		Voucher voucher = null;

		if(this.cst.equals(Campaign.CampaignStatusType.EXPIRED) ||
			this.cst.equals(Campaign.CampaignStatusType.CANCELLED)) {

			return voucher;
		}

		if(this.strategy.equals("A")) {
			Random r = new Random();
			int index = r.nextInt(this.observers.size());
			String email = this.observers.get(index).email;
			voucher = this.generateVoucher(email, "GiftVoucher", 100);
		}

		if(this.strategy.equals("B")) {
			String email = null;
			int max = 0;
			for(User u : this.observers) {
				int nrOfUsedVouchers = 0;
				for(Voucher v : u.vouchers.get(this.id)) {
					if(v.vst.equals(Voucher.VoucherStatusType.USED));
					nrOfUsedVouchers++;
				}
				if(nrOfUsedVouchers > max) {
					email = u.email;
					max = nrOfUsedVouchers;
				}
			}
			voucher = this.generateVoucher(email, "LoyaltyVoucher", 50);
		}

		if(this.strategy.equals("C")) {
			String email = null;
			int min = 100000;
			for(User u : this.observers) {
				int nrOfReceivedVouchers = 0;
				nrOfReceivedVouchers = u.vouchers.get(this.id).size();
				if(nrOfReceivedVouchers < min) {
					email = u.email;
					min = nrOfReceivedVouchers;
				}
			}
			voucher = this.generateVoucher(email, "GiftVoucher", 100);
		}

		return voucher;
	}
}