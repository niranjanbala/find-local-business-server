package biz.finder.ipl.get;

import java.util.HashMap;
import java.util.Map;

import com.google.apphosting.api.ApiProxy.Environment;

public class TestEnvironment implements Environment {

	
	@Override
	public boolean isLoggedIn() {
		return true;
	}
	
	@Override
	public boolean isAdmin() {
		return true;
	}
	
	@Override
	public String getVersionId() {
		return "1.0";
	}
	
	@Override
	@Deprecated
	public String getRequestNamespace() {
		return null;
	}
	
	@Override
	public long getRemainingMillis() {
		return 0;
	}
	
	@Override
	public String getEmail() {
		return null;
	}
	
	@Override
	public String getAuthDomain() {
		return null;
	}
	
	@Override
	public Map<String, Object> getAttributes() {
		return new HashMap<String, Object>();
	}
	
	@Override
	public String getAppId() {
		return "find-business";
	}

}
