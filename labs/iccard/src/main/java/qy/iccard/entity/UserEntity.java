package qy.iccard.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/*****
 * 
 * @Entity
 */
@SuppressWarnings("serial")
@Entity
@Table(name="t_user")
public class UserEntity implements Serializable {
	
	/*****
	 * 
	 */
	protected Long id;
  
	/*****
	 * 
	 */
	protected String userName;
  
	/*****
	 * 
	 */
	protected String email;
  
	/*****
	 * 
	 */
	protected String status;
  
	/*****
	 * 
	 */
	protected Integer age;
  
	/*****
	 * 
	 */
	protected Date birthDay;
  
	/*****
	 * 
	 */
	protected Float height;
  
  
  
	/*****
	 * 
	 */
	protected String password;
  
	public UserEntity(){
	}
	
	
	/*****
	 * 
	 * @return
	 */
	@Id	@GeneratedValue(strategy=GenerationType.IDENTITY)	@Column(name="ID")
	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Column(name="USER_NAME")
	public String getUserName() {
		return this.userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Column(name="EMAIL")
	public String getEmail() {
		return this.email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Column(name="STATUS")
	public String getStatus() {
		return this.status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Column(name="AGE")
	public Integer getAge() {
		return this.age;
	}
	
	public void setAge(Integer age) {
		this.age = age;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="BIRTH_DAY")
	public Date getBirthDay() {
		return this.birthDay;
	}
	
	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Column(name="HEIGHT")
	public Float getHeight() {
		return this.height;
	}
	
	public void setHeight(Float height) {
		this.height = height;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Column(name="PASSWORD")
	public String getPassword() {
		return this.password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
}