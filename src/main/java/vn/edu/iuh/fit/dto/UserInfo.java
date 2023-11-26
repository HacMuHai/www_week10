package vn.edu.iuh.fit.dto;

public record UserInfo (String username,String password,boolean enable, String [] authorities) {
}
