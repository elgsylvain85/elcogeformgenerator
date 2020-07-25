package cd.gcd.formgenerator.icons;

import cd.gcd.formgenerator.Resource;

/**
 * To have a icon predefini
 * 
 * @author mulambas
 *
 */
public enum Icon implements Resource {

	ADD("add.png"), DATA_SETTING("data_setting.png"), EDIT("edit.png"), FILTER("filter.png"), GROUP("group.png"), LOCK(
			"lock.png"), LOG("log.png"), MODELISATION("modelisation.png"), PERMISSION("permission.png"), REPORT(
					"report.png"), RESTRICTION("restriction.png"), SETTING(
							"setting.png"), USER_LOCK("user_lock.png"), USER("user.png"), HOME("home.png"), EXIT(
									"exit.png"), POWER("power.png"), VALID("valid.png"), CANCEL("cancel.png"), REFRESH(
											"refresh.png"), VIEW("view.png"), DATA_RESTRICTION(
													"data_restriction.png"), DATA("data.png"), SEARCH(
															"search.png"), UPDATE("update.png"), DELETE(
																	"delete.png"), EXPORT("export.png"), IMPORT(
																			"import.png"), RESTORE(
																					"restore.png"), CONTRAINTE(
																							"contrainte.png"), TOOLS(
																									"tools.png"), MENU(
																											"menu.png"), KEY(
																													"key.png"), ABOUT(
																															"about.png"), EXPRESSION(
																																	"expression.png"), ALL(
																																			"all.png"), NEXT(
																																					"next.png"), PREVIOUS(
																																							"previous.png"), RUN(
																																									"run.png"), QUESTION(
																																											"question.png"), EMPTY(
																																													"empty.png"), ANIM_INPROGRESS(
																																															"anim_inprogress.gif"), EXCEL(
																																																	"excel.png"), PDF(
																																																			"pdf.png");

	String filename;

	Icon(String filename) {
		this.filename = filename;
	}

	@Override
	public String url() {

		String fullpath = (Icon.class.getPackage().getName()).replace('.', '/') + '/';
		String url = fullpath + this.filename;

		return url;
	}
}