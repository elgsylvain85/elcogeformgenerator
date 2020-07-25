package test;

import java.util.Date;
import java.util.List;

import cd.gcd.formgenerator.annotation.DisableField;
import cd.gcd.formgenerator.annotation.IgnoreField;

//import lombok.Data;

//@Data
public class POJO {

	@DisableField
	public Integer id;
	public String name;
	public String adress;
	public Date birthday;
	public List<Integer> phones;
//	public Integer childs;
//	public Double banksold;
//	public POJO pojo;
//	public POJOEnum pojoenum;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAdress() {
		return adress;
	}
	public void setAdress(String adress) {
		this.adress = adress;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public List<Integer> getPhones() {
		return phones;
	}
	public void setPhones(List<Integer> phones) {
		this.phones = phones;
	}
}



 enum POJOEnum{
	POJO1, POJO2;
}
 
