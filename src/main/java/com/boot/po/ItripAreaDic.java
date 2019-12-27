package com.boot.po;

import java.io.Serializable;
import java.util.Date;

public class ItripAreaDic implements Serializable {
    private Long id;

    private String name;

    private String areano;

    private Long parent;

    private Integer isactivated;

    private Integer istradingarea;

    private Integer ishot;

    private Integer level;

    private Integer ischina;

    private String pinyin;

    private Date creationdate;

    private Long createdby;

    private Date modifydate;

    private Long modifiedby;

    public ItripAreaDic(Long id, String name, String areano, Long parent, Integer isactivated, Integer istradingarea, Integer ishot, Integer level, Integer ischina, String pinyin, Date creationdate, Long createdby, Date modifydate, Long modifiedby) {
        this.id = id;
        this.name = name;
        this.areano = areano;
        this.parent = parent;
        this.isactivated = isactivated;
        this.istradingarea = istradingarea;
        this.ishot = ishot;
        this.level = level;
        this.ischina = ischina;
        this.pinyin = pinyin;
        this.creationdate = creationdate;
        this.createdby = createdby;
        this.modifydate = modifydate;
        this.modifiedby = modifiedby;
    }

    public ItripAreaDic() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getAreano() {
        return areano;
    }

    public void setAreano(String areano) {
        this.areano = areano == null ? null : areano.trim();
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public Integer getIsactivated() {
        return isactivated;
    }

    public void setIsactivated(Integer isactivated) {
        this.isactivated = isactivated;
    }

    public Integer getIstradingarea() {
        return istradingarea;
    }

    public void setIstradingarea(Integer istradingarea) {
        this.istradingarea = istradingarea;
    }

    public Integer getIshot() {
        return ishot;
    }

    public void setIshot(Integer ishot) {
        this.ishot = ishot;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getIschina() {
        return ischina;
    }

    public void setIschina(Integer ischina) {
        this.ischina = ischina;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin == null ? null : pinyin.trim();
    }

    public Date getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(Date creationdate) {
        this.creationdate = creationdate;
    }

    public Long getCreatedby() {
        return createdby;
    }

    public void setCreatedby(Long createdby) {
        this.createdby = createdby;
    }

    public Date getModifydate() {
        return modifydate;
    }

    public void setModifydate(Date modifydate) {
        this.modifydate = modifydate;
    }

    public Long getModifiedby() {
        return modifiedby;
    }

    public void setModifiedby(Long modifiedby) {
        this.modifiedby = modifiedby;
    }
}