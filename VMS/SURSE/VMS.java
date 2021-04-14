import java.util.*;
import java.time.*;

public class VMS {
	LinkedList<Campaign> campaigns;
	LinkedList<User> users;

	public VMS() {
		campaigns = new LinkedList<Campaign>();
		users = new LinkedList<User>();
	}

	public LinkedList<Campaign> getCampaigns() {
		return this.campaigns;
	}

	public Campaign getCampaign(Integer id) {
		for(Campaign c : this.campaigns) {
			if(c.id == id)
				return c;
		}
		return null;
	}

	public void addCampaign(Campaign campaign) {
		this.campaigns.add(campaign);
	}

	public void updateCampaign(Integer id, Campaign campaign, LocalDateTime currDate) { //modific un pic antetul functiei,
											//pentru ca atunci cand o apelez sa trimit notificari 
											//observatorilor ca a fost anulata campania
		Campaign c;
		Notification not;

		for(int i = 0; i < this.campaigns.size(); i++) {
			if(this.campaigns.get(i).id == id) {
				c = this.campaigns.get(i);
				if(c.cst.equals(Campaign.CampaignStatusType.NEW)) {
					this.campaigns.get(i).name = campaign.name;
					this.campaigns.get(i).description = campaign.description;
					this.campaigns.get(i).startDate = campaign.startDate;
					this.campaigns.get(i).endDate = campaign.endDate;
					this.campaigns.get(i).nrVouchers = campaign.nrVouchers;
					
					if(this.campaigns.get(i).nrVouchers < this.campaigns.get(i).nrCurrAvVouchers)
						this.campaigns.get(i).nrCurrAvVouchers = this.campaigns.get(i).nrVouchers;
				}
				else {
					if(c.cst.equals(Campaign.CampaignStatusType.STARTED)) {
						this.campaigns.get(i).nrVouchers = campaign.nrVouchers;
						this.campaigns.get(i).endDate = campaign.endDate;
						
						if(this.campaigns.get(i).nrVouchers < this.campaigns.get(i).nrCurrAvVouchers)
							this.campaigns.get(i).nrCurrAvVouchers = this.campaigns.get(i).nrVouchers;
					}
				}

				not = new Notification(Notification.NotificationType.EDIT, currDate, c.id);
				this.campaigns.get(i).notifyAllObservers(not);
			}
		}
	}

	public void cancelCampaign(Integer id, LocalDateTime currDate) { //modific un pic antetul functiei,
											//pentru ca atunci cand o apelez sa trimit notificari 
											//observatorilor ca a fost anulata campania

		Campaign c;
		Notification not;

		for(int i = 0; i < this.campaigns.size(); i++) {
			if(this.campaigns.get(i).id == id) {
				c = this.campaigns.get(i);
				if(c.cst.equals(Campaign.CampaignStatusType.NEW) ||
					this.campaigns.get(i).cst.equals(Campaign.CampaignStatusType.STARTED)) {

					this.campaigns.get(i).cst = Campaign.CampaignStatusType.CANCELLED;
				}

				not = new Notification(Notification.NotificationType.CANCEL, currDate, c.id);

				this.campaigns.get(i).notifyAllObservers(not);
			}
		}
	}

	public LinkedList<User> getUsers() {
		return this.users;
	}

	public void addUser(User user) {
		this.users.add(user);
	}

	private static final VMS instance = new VMS();

	public static VMS getInstance() {
		return instance;
	}
}