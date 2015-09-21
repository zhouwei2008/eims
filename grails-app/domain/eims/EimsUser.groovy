package eims

class EimsUser {
    String userid
    String userpwd
    int   counts
    int   maxcounts
    Date  expiredate
    String usersts
    String ips

    static constraints = {
        userid(maxsize:64)
        userpwd(maxsize:128)
        usersts(maxsize:10)
        ips(nullable:true,maxsize:128)
    }
}
