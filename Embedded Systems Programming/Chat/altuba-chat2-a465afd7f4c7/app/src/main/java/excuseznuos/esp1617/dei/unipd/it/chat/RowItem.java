package excuseznuos.esp1617.dei.unipd.it.chat;

public class RowItem {

    private final String member_name;
    private final int profile_pic_id;
    private final String status;
    private final String contactType;

    public RowItem(String member_name, int profile_pic_id, String status, String contactType) {
        this.member_name = member_name;
        this.profile_pic_id = profile_pic_id;
        this.status = status;
        this.contactType = contactType;
    }

    public String getMember_name() {
        return member_name;
    }

    public int getProfile_pic_id() {
        return profile_pic_id;
    }

    public String getStatus() { return status; }

    public String getContactType() { return contactType; }
}
