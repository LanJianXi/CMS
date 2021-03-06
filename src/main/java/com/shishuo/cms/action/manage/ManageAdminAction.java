/*
 *	Copyright © 2013 Changsha Shishuo Network Technology Co., Ltd. All rights reserved.
 *	长沙市师说网络科技有限公司 版权所有
 *	http://www.shishuo.com
 */

package com.shishuo.cms.action.manage;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shishuo.cms.entity.Admin;
import com.shishuo.cms.entity.vo.JsonVo;
import com.shishuo.cms.util.SSUtils;

/**
 * 管理员action
 * 
 * @author Zhangjiale
 * 
 */
@Controller
@RequestMapping("/manage/admin")
public class ManageAdminAction extends ManageBaseAction {

	/**
	 * 进入添加admin页面
	 * 
	 */
	@RequestMapping(value = "/add.htm", method = RequestMethod.GET)
	public String addUser(ModelMap modelMap) {
		modelMap.put("adminName", "");
		modelMap.put("email", "");
		return "manage/admin/add";
	}

	/**
	 * 
	 * 进入管理员管理页面
	 */
	@RequestMapping(value = "/manage.htm", method = RequestMethod.GET)
	public String manage(ModelMap modelMap) {
		modelMap.put("list", adminService.getAllList());
		return "manage/admin/manage";
	}

	/**
	 * 添加Admin
	 * 
	 */
	@ResponseBody
	@RequestMapping(value = "/addNew.json", method = RequestMethod.POST)
	public JsonVo<String> addNewUser(
			@RequestParam(value = "adminName") String adminName,
			@RequestParam(value = "password") String password) {
		JsonVo<String> json = new JsonVo<String>();
		try {
			if (adminName.equals("")) {
				json.getErrors().put("adminName", "管理员名称不能为空");
			}
			if (StringUtils.isBlank(password)) {
				json.getErrors().put("password", "管理员密码不能为空");
			} else if (password.length() < 6) {
				json.getErrors().put("password", "密码不能小于6位");
			} else if (password.length() > 16) {
				json.getErrors().put("password", "密码不能大于16位");
			}
			Admin admin = adminService.getAdminByName(adminName);
			if (admin != null) {
				json.getErrors().put("adminName", "管理员名称不能重复");
			}

			// 检测校验结果
			validate(json);
			adminService.addAdmin(SSUtils.toText(adminName.trim()),
					password);
			json.setResult(true);
		} catch (Exception e) {
			json.setResult(false);
			json.setMsg(e.getMessage());
		}
		return json;
	}

	/**
	 * 进入单个admmin页面
	 * 
	 */
	@RequestMapping(value = "/update.htm", method = RequestMethod.GET)
	public String update(
			ModelMap modelMap, HttpServletRequest request) {
		Admin sessionAdmin = this.getAdmin(request);
		Admin admin = adminService.getAdminById(sessionAdmin.getAdminId());
		modelMap.put("admin", admin);
		return "manage/admin/update";
	}

	/**
	 * 修改指定的admin资料
	 * 
	 */
	@ResponseBody
	@RequestMapping(value = "/update.json", method = RequestMethod.POST)
	public JsonVo<String> updateAdmin(@RequestParam(value = "password") String password,
			@RequestParam(value = "newpwd") String newpwd,
			HttpServletRequest request) {
		JsonVo<String> json = new JsonVo<String>();
		try {
			if (StringUtils.isBlank(newpwd)) {
				json.getErrors().put("newpwd", "密码不能为空");
			}
			if (password.length() < 6) {
				json.getErrors().put("newpwd", "密码不能小于6位数");
			}
			if (password.length() > 16) {
				json.getErrors().put("newpwd", "密码不能大于16位数");
			}
			Admin admin = this.getAdmin(request);
			if(!adminService.checkPwd(admin.getAdminId(),SSUtils.toText(password)))
			{
				json.getErrors().put("password", "密码错误");
			}
				// 检测校验结果
			validate(json);

			adminService.updateAdminByAmdinId(admin.getAdminId(),
					SSUtils.toText(newpwd));
			json.setResult(true);
		} catch (Exception e) {
			json.setResult(false);
			json.setMsg(e.getMessage());
		}
		return json;
	}

	/**
	 * 删除管理员
	 * 
	 */

	@ResponseBody
	@RequestMapping(value = "/delete.json", method = RequestMethod.POST)
	public JsonVo<String> delete(@RequestParam(value = "adminId") long adminId) {
		JsonVo<String> json = new JsonVo<String>();
		try {
			adminService.deleteAdmin(adminId);
			json.setResult(true);
		} catch (Exception e) {
			json.setResult(false);
			json.setMsg(e.getMessage());
		}
		return json;
	}

}
