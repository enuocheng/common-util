/*
package com.example.util.aop.authPerm;

import com.moredian.onpremise.core.adapter.CacheAdapter;
import com.moredian.onpremise.core.aop.authPerm.annotation.AuthPermAnnotation;
import com.moredian.onpremise.core.common.constants.OpenApiConstants;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import com.moredian.onpremise.core.common.enums.OnpremiseErrorEnum;
import com.moredian.onpremise.core.mapper.AccountAuthMapper;
import com.moredian.onpremise.core.mapper.AccountMapper;
import com.moredian.onpremise.core.mapper.AppMapper;
import com.moredian.onpremise.core.mapper.AuthModuleMapper;
import com.moredian.onpremise.core.model.domain.Account;
import com.moredian.onpremise.core.model.domain.AccountAuth;
import com.moredian.onpremise.core.model.response.AppResponse;
import com.moredian.onpremise.core.model.response.AuthModuleResponse;
import com.moredian.onpremise.core.model.response.UserLoginResponse;
import com.moredian.onpremise.core.utils.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


*/
/**
 * 自定义切面，和自定义注解联合使用完成日志收集
 * @author Administrator
 *
 *//*

@Component
@Aspect
@Slf4j
public class AuthPermAop {

	@Autowired
	private HttpServletRequest request;
	@Autowired
	private AccountMapper accountMapper;
	@Autowired
	private AccountAuthMapper accountAuthMapper;
	@Autowired
	private AuthModuleMapper authModuleMapper;
	@Autowired
	private AppMapper appMapper;

	@Pointcut(value="@annotation(com.moredian.onpremise.core.aop.authPerm.annotation.AuthPermAnnotation)")
	public void pointCut(){
		
	}
	
	@Around(value="pointCut()&&@annotation(authPermAnnotation)")
	public Object aroundAdvice(ProceedingJoinPoint pjp, AuthPermAnnotation authPermAnnotation) throws Throwable{
		String sessionId = request.getSession().getId();
		UserLoginResponse loginResponse = CacheAdapter.getLoginInfo(sessionId);
		if (loginResponse==null){
			loginResponse = CacheAdapter.getLoginInfo(OpenApiConstants.OPEN_API_LOGIN_ACCOUNT);
		}
		AssertUtil.isNullOrEmpty(loginResponse, OnpremiseErrorEnum.ACCOUNT_NOT_LOGIN);
		Account account = accountMapper.getAccountInfo(loginResponse.getAccountId(),loginResponse.getOrgId());
		log.info("account accountId={},accountGrade={},uri={}", account.getAccountId(), account.getAccountGrade(), request.getRequestURI());
		AssertUtil.isNullOrEmpty(account, OnpremiseErrorEnum.ACCOUNT_NOT_FIND);
		AssertUtil.isTrue(account.getAccountGrade()==2 || account.getAccountGrade()==1,OnpremiseErrorEnum.ACCOUNT_OPER_AUTH_NOT_ALLOW);

		if (account.getAccountGrade() == 2) {
			AccountAuth accountAuth = accountAuthMapper.getOneByAccountId(loginResponse.getAccountId(), loginResponse.getOrgId());
			AssertUtil.isNullOrEmpty(accountAuth, OnpremiseErrorEnum.ACCOUNT_AUTH_NOT_FIND);
			log.info("accountAuth accountId={},manageAppId={},manageModuleId={}", accountAuth.getAccountId(), accountAuth.getManageAppId(), accountAuth.getManageModuleId());
			List<AppResponse> appResponses = appMapper.getUnOpenAppList(loginResponse.getOrgId());
			List<Integer> appTypes = new ArrayList<>();
			if (!CollectionUtils.isEmpty(appResponses)) {
				for (AppResponse appResponse: appResponses) {
					appTypes.add(appResponse.getAppType());
				}
			}

			List<AuthModuleResponse> authModuleResponses = authModuleMapper.listById(Arrays.asList(accountAuth.getManageModuleId().split(SymbolConstants.COMMA_SYMBOL)), loginResponse.getOrgId(), appTypes);
			log.info("accountAuth accountId={},manageModuleId={}", accountAuth.getAccountId(), authModuleResponses.stream().map(AuthModuleResponse::getModuleId).collect(Collectors.toList()));
			AssertUtil.isTrue(!authModuleResponses.isEmpty(), OnpremiseErrorEnum.ACCOUNT_AUTH_NOT_ALLOW_FOR_INTERFACE);

			boolean isAllow = false;
			for (AuthModuleResponse authModuleResponse : authModuleResponses) {
				if (StringUtils.isNotEmpty(authModuleResponse.getInterfaceUri()) && authModuleResponse.getInterfaceUri().contains(request.getRequestURI())) {
					isAllow = true;
					break;
				}
			}
			AssertUtil.isTrue(isAllow, OnpremiseErrorEnum.ACCOUNT_AUTH_NOT_ALLOW_FOR_INTERFACE);
		}
		return pjp.proceed() ;
	}
}
*/
