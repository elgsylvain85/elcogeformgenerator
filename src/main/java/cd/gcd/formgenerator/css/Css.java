package cd.gcd.formgenerator.css;

import cd.gcd.formgenerator.Resource;

/**
 * To have a css predefini
 * 
 * @author mulambas
 *
 */
public enum Css implements Resource {

	DarkTheme("DarkTheme.css");

	String filename;

	Css(String filename) {
		this.filename = filename;
	}

	@Override
	public String url() {
		String fullpath = (Css.class.getPackage().getName()).replace('.', '/') + '/';
		String url = fullpath + this.filename;

		return url;
	}
}