package com.quark.model;

import java.io.Serializable;

/**
*
* @ClassName: HuanxinUser
* @Description: 环信 查找返回modle
* @author howe
* @date 2015-2-5 上午10:53:41
*
*/
public class HuanxinUser implements Serializable {
	private String uid;//	String	返回环信id。用户认u开头，商家以c开头
	public String name;//	String	姓名
	private String avatar;//	String	头像
    /*
    -1-普通商户，0-个人认证，1-企业认证，3-经纪人认证
     */
    private int type ; //用户类型
	private String namePinyin;  //名字转化为拼音 为了列表显示

    public int creditworthiness; //信誉
    public int sex ; //
    public int earnest_money; //诚意金
    public int certification; //认证状态
    public int age;
    public String introduction;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getEarnest_money() {
        return earnest_money;
    }

    public void setEarnest_money(int earnest_money) {
        this.earnest_money = earnest_money;
    }

    public int getCertification() {
        return certification;
    }

    public void setCertification(int certification) {
        this.certification = certification;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getSex() {

        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String city; //城市

	
	public String getNamePinyin() {
		return namePinyin;
	}
	public void setNamePinyin(String namePinyin) {
		this.namePinyin = namePinyin;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCreditworthiness() {
        return creditworthiness;
    }

    public void setCreditworthiness(int creditworthiness) {
        this.creditworthiness = creditworthiness;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    @Override
	public String toString() {
		return "HuanxinUser [uid=" + uid + ", name=" + name + ", avatar="
				+ avatar + ", type= "+ type +"]";
	}
}
