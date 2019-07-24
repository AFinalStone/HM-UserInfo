package com.hm.iou.userinfo.bean

class PayPackageResBean {

    /**
     * countSign : 0
     * firstPackage : {"actualPrice":0,"concessions":"string","content":"string","goodsId":"string","originalPrice":0,"packageId":0,"rechargeSign":0}
     * packageRespList : [{"actualPrice":0,"concessions":"string","content":"string","goodsId":"string","originalPrice":0,"packageId":0,"rechargeSign":0}]
     * signUnitPrice : 0
     */

    val countSign: Int = 0
    val firstPackage: FirstPackageBean? = null
    val signUnitPrice: Int = 0
    val packageRespList: List<PackageRespListBean>? = null

    class FirstPackageBean {
        /**
         * actualPrice : 0
         * concessions : string
         * content : string
         * goodsId : string
         * originalPrice : 0
         * packageId : 0
         * rechargeSign : 0
         */
        val actualPrice: Int = 0
        val concessions: String? = null
        val content: String? = null
        val goodsId: String? = null
        val originalPrice: Int = 0
        val packageId: Int = 0
        val rechargeSign: Int = 0

    }

    class PackageRespListBean {
        /**
         * actualPrice : 0
         * concessions : string
         * content : string
         * goodsId : string
         * originalPrice : 0
         * packageId : 0
         * rechargeSign : 0
         */

        val actualPrice: Int = 0
        val concessions: String? = null
        val content: String? = null
        val goodsId: String? = null
        val originalPrice: Int = 0
        val packageId: Int = 0
        val rechargeSign: Int = 0
    }
}

