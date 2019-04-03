package com.hm.iou.userinfo.bean;

import java.util.List;

import lombok.Data;

/**
 * @author : syl
 * @Date : 2018/5/21 16:49
 * @E-Mail : shiyaolei@dafy.com
 */
@Data
public class PayPackageResBean {


    /**
     * countSign : 0
     * firstPackage : {"actualPrice":0,"concessions":"string","content":"string","goodsId":"string","originalPrice":0,"packageId":0,"rechargeSign":0}
     * packageRespList : [{"actualPrice":0,"concessions":"string","content":"string","goodsId":"string","originalPrice":0,"packageId":0,"rechargeSign":0}]
     * signUnitPrice : 0
     */

    private int countSign;
    private FirstPackageBean firstPackage;
    private int signUnitPrice;
    private List<PackageRespListBean> packageRespList;

    @Data
    public static class FirstPackageBean {
        /**
         * actualPrice : 0
         * concessions : string
         * content : string
         * goodsId : string
         * originalPrice : 0
         * packageId : 0
         * rechargeSign : 0
         */
        private int actualPrice;
        private String concessions;
        private String content;
        private String goodsId;
        private int originalPrice;
        private int packageId;
        private int rechargeSign;

    }

    @Data
    public static class PackageRespListBean {
        /**
         * actualPrice : 0
         * concessions : string
         * content : string
         * goodsId : string
         * originalPrice : 0
         * packageId : 0
         * rechargeSign : 0
         */

        private int actualPrice;
        private String concessions;
        private String content;
        private String goodsId;
        private int originalPrice;
        private int packageId;
        private int rechargeSign;
    }
}