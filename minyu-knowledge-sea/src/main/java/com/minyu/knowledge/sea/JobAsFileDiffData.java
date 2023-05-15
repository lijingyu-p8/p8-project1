package com.minyu.knowledge.sea;

/**
 * @Description: 文件形式的差异结果
 * @Author: lijingyu
 * @CreateTime: 2023-05-13  21:12
 */
public class JobAsFileDiffData {
    private int id;
    /**
     * 传入参数
     */
    private String paramIn;
    /**
     * 关键字
     */
    private String keyWord;
    /**
     * 新版本返回参数（无results）
     */
    private String newParamOut;
    /**
     * 旧版本返回参数（无results）
     */
    private String oldParamOut;
    /**
     * 新版本职位数
     */
    private String newJobNum;
    /**
     * debug total hit
     */
    private String debugTotalHit;
    /**
     * 老版本职位数
     */
    private String oldJobNum;
    /**
     * 差异原因
     */
    private String diffReason;
    /**
     * 新版本有的职位
     */
    private String newHasJob;
    /**
     * 老版本有的职位
     */
    private String oldHasJob;
    /**
     * 新版本dsl
     */
    private String newDsl;
    /**
     * 老版本dsl
     */
    private String oldDsl;
    /**
     * 新版本职位列表
     */
    private String newAllJob;
    /**
     * 老版本职位列表
     */
    private String oldAllJob;
    /**
     * 新版本rank
     */
    private String newRank;
    /**
     * 老版本rank
     */
    private String oldRank;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getParamIn() {
        return paramIn;
    }

    public void setParamIn(String paramIn) {
        this.paramIn = paramIn;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getNewParamOut() {
        return newParamOut;
    }

    public void setNewParamOut(String newParamOut) {
        this.newParamOut = newParamOut;
    }

    public String getOldParamOut() {
        return oldParamOut;
    }

    public void setOldParamOut(String oldParamOut) {
        this.oldParamOut = oldParamOut;
    }

    public String getNewJobNum() {
        return newJobNum;
    }

    public void setNewJobNum(String newJobNum) {
        this.newJobNum = newJobNum;
    }

    public String getDebugTotalHit() {
        return debugTotalHit;
    }

    public void setDebugTotalHit(String debugTotalHit) {
        this.debugTotalHit = debugTotalHit;
    }

    public String getOldJobNum() {
        return oldJobNum;
    }

    public void setOldJobNum(String oldJobNum) {
        this.oldJobNum = oldJobNum;
    }

    public String getDiffReason() {
        return diffReason;
    }

    public void setDiffReason(String diffReason) {
        this.diffReason = diffReason;
    }

    public String getNewHasJob() {
        return newHasJob;
    }

    public void setNewHasJob(String newHasJob) {
        this.newHasJob = newHasJob;
    }

    public String getOldHasJob() {
        return oldHasJob;
    }

    public void setOldHasJob(String oldHasJob) {
        this.oldHasJob = oldHasJob;
    }

    public String getNewDsl() {
        return newDsl;
    }

    public void setNewDsl(String newDsl) {
        this.newDsl = newDsl;
    }

    public String getOldDsl() {
        return oldDsl;
    }

    public void setOldDsl(String oldDsl) {
        this.oldDsl = oldDsl;
    }

    public String getNewAllJob() {
        return newAllJob;
    }

    public void setNewAllJob(String newAllJob) {
        this.newAllJob = newAllJob;
    }

    public String getOldAllJob() {
        return oldAllJob;
    }

    public void setOldAllJob(String oldAllJob) {
        this.oldAllJob = oldAllJob;
    }

    public String getNewRank() {
        return newRank;
    }

    public void setNewRank(String newRank) {
        this.newRank = newRank;
    }

    public String getOldRank() {
        return oldRank;
    }

    public void setOldRank(String oldRank) {
        this.oldRank = oldRank;
    }

    @Override
    public String toString() {
        return "JobAsFileDiffData{" +
                "id=" + id +
                ", paramIn='" + paramIn + '\'' +
                ", keyWord='" + keyWord + '\'' +
                ", newParamOut='" + newParamOut + '\'' +
                ", oldParamOut='" + oldParamOut + '\'' +
                ", newJobNum='" + newJobNum + '\'' +
                ", debugTotalHit='" + debugTotalHit + '\'' +
                ", oldJobNum='" + oldJobNum + '\'' +
                ", diffReason='" + diffReason + '\'' +
                ", newHasJob='" + newHasJob + '\'' +
                ", oldHasJob='" + oldHasJob + '\'' +
                ", newDsl='" + newDsl + '\'' +
                ", oldDsl='" + oldDsl + '\'' +
                ", newAllJob='" + newAllJob + '\'' +
                ", oldAllJob='" + oldAllJob + '\'' +
                ", newRank='" + newRank + '\'' +
                ", oldRank='" + oldRank + '\'' +
                '}';
    }
}