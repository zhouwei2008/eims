package eims


class EimsItem {
    String channel
    String channelType
    String target
    String content
    String opersts
    String userid
    Date createdate
    Date closedate
    int opercounts

    static constraints = {
        userid(maxsize:64,nullable:true)
        channel(maxsize:64)
        channelType(inList: ['SMS','EMAIL'])
        target(maxsize:128)
        content(maxsize:512,nullable:true)
        opersts(inList:['Y','N','F'])
    }
}
