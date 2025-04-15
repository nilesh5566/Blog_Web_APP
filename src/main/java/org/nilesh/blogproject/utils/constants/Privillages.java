package org.nilesh.blogproject.utils.constants;

public enum Privillages {
    RESET_ANY_USER_PASSWORD(1l,"RESET_ANY_USER_PASSWORD"),
    ACCESS_ADMIN_PANEL(2l,"ACCESS_ADMIN_PANEL");
    private Long Id;
    private String auString;
    private Privillages(Long Id,String auString ){
        this.Id=Id;
        this.auString=auString;
    }
    public Long getId(){
        return  Id;
    }
    public String getAuthorityString(){
        return auString;
    }
    

    
}