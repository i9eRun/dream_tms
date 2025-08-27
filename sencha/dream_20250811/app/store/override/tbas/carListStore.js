Ext.define('dream.store.override.tbas.carListStore', {
    override: 'dream.store.tbas.carListStore',
    
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            proxy: {
                type: 'ajax',
                url: dream.util.Common.BASE_URL + '/tbas/carlist',
                extraParam:{USER_CET_CD:dream.util.Common.LOGIN_USER_CET_CD},
                reader: {
                    type: 'json',
                    rootProperty: 'data'
                }
            }
        }, cfg)]);
    }
    
});