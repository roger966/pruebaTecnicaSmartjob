/**
 * 
 */
package co.com.crudSec.dto;

/**
 * @author roger
 *
 */
public class TelefonoDTO {
    private String number;
    private String citycode;
    private String contrycode;
    
	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}
	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}
	/**
	 * @return the citycode
	 */
	public String getCitycode() {
		return citycode;
	}
	/**
	 * @param citycode the citycode to set
	 */
	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}
	/**
	 * @return the countrycode
	 */
	public String getContrycode() {
		return contrycode;
	}
	/**
	 * @param countrycode the countrycode to set
	 */
	public void setContrycode(String contrycode) {
		this.contrycode = contrycode;
	}
}
