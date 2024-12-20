package vn.uit.sangSoftwareDesgin.softwareDesginProject.Constants;

public interface JwtConstants
{
    public static final String HEADER_STRING = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String SIGNING_KEY = "sangdoan_a83acdd1f5b19d295dd1283ebebce69104fd0bbc0bfbe61bfe83958dfbc04546";
    public static final int ACCESS_TOKEN_VALIDITY_SECONDS = 5*60*60;
    public static final int REFRESH_TOKEN_VALIDITY_SECONDS = 24*60*60;

    public static final String AUTHORITIES_KEY = "scopes";
}

