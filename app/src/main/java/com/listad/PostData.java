package com.listad;

public class PostData {
    private String title;
    private String content;
    private String no;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public PostData(String title, String content, String no) {

        this.title = title;
        this.content = content;
        this.no = no;
    }
}
