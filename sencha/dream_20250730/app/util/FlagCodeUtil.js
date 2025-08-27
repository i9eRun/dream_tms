Ext.define('DreamNalgae.util.FlagCodeUtil', {
    singleton:true,

    setFlagCombo: function (com, flagType, addName='', defaultValue='') {
        const combo = Ext.ComponentQuery.query(`#${com.id}`)[0];
        if (!combo) {
            console.warn(`콤보박스 ID(${com.id})가 존재하지 않습니다.`);
            return;
        }

        const codeData = DreamNalgae.data.FlagCodeList.getCodeData(flagType);
        if (!codeData) {
            Ext.Msg.alert('오류', `정의되지 않은 코드 유형입니다: ${flagType}`);
            return;
        }

        const records = [];
        
        if (addName) {
            records.push({ codeCd:'', codeNm:addName });
        }

        for (let i=0;i<codeData.codes.length;i++) {
            records.push({
                codeCd:codeData.codes[i],
                codeNm:codeData.names[i]
            });
        }

        const store = Ext.create('Ext.data.Store', {
            fields:['codeCd','codeNm'],
            data:records
        });

        combo.setStore(store);
        combo.displayField = 'codeNm';
        combo.valueField = 'codeCd';
        combo.queryMode = 'local';
        combo.editable = false;
        combo.setValue(defaultValue);

    },

    // ExtJS로 마이플랫폼 코드정의 dataset 설정 로직 변환
    // 목적 : 콤보박스 또는 그리드 셀에 코드 데이타셋 바인딩 처리

    getCodeInfoMap: function () {
        return {
            BOOK_PAN:     { table: 'TMS_BOOK_PAN',     codeField: 'BOK_PAN_GB',    nameField: 'BOK_PAN_NM' },
            DEPT:         { table: 'TMS_DEPT',         codeField: 'N_DEPT_CD',     nameField: 'DEPT_NM' },
            MENU:         { table: 'TMS_MENU_M',       codeField: 'MENU_ID',       nameField: 'MENU_NM' },
            UNSONG_COMP:  { table: 'TMS_VAN_CHULGO',   codeField: 'UNSONG_COMP_NM',nameField: 'UNSONG_COMP_NM' },
            UNSONG_DRV:   { table: 'TMS_VAN_CHULGO',   codeField: 'UNSONG_DRV_NM', nameField: 'UNSONG_DRV_NM' },
            VAN_JIYUK_NM: { table: 'TMS_VAN_DANGA',    codeField: 'VAN_JIYUK_NM',  nameField: 'VAN_JIYUK_NM' },
            VAN_TEL_NO:   { table: 'TMS_VAN_DRIVER',   codeField: 'TEL_NO',        nameField: 'TEL_NO' },
            PLT_CUST:     { table: 'TMS_PLT_CUST',     codeField: 'PLT_CUST_NO',   nameField: 'PLT_CUST_NM' },
            CALL_TEAM:    { table: 'TMS_VAN_CHULGO',   codeField: 'VAN_CALL_TEAM_NM', nameField: 'VAN_CALL_TEAM_NM' },
            DEPT_JO:      { table: 'TMS_DEPT',         codeField: 'DEPT_CD',       nameField: 'NEW_CHO_NM' },
            BOOK_DIV:     { table: 'TMS_BOOK_DIV',     codeField: 'BOK_DIV_GB',    nameField: 'BOK_DIV_NM' },
            CODE:         { table: 'TMS_CODE_M',       codeField: 'CODE_CD',       nameField: 'CODE_NM' },
            N_DEPT:       { table: 'TMS_DEPT',         codeField: 'N_DEPT_CD',     nameField: 'DEPT_NM' },
            CHUNGU_UNIT:  { table: 'TMS_CHUNGU_INFO',  codeField: 'CHUNGU_UNIT',   nameField: 'CHUNGU_UNIT' }
        };
    },

    loadCodeStore: function (com, table_name, options = {}) {
        const combo = Ext.ComponentQuery.query(`#${com.id}`)[0];
        if (!combo) {
            console.warn(`콤보박스 ID(${com.id})가 존재하지 않습니다.`);
            return;
        }

        const codeMap = this.getCodeInfoMap();
        const info = codeMap[table_name];

        if (!info) {
            Ext.Msg.alert('오류', `정의되지 않은 코드 타입입니다: ${table_name}`);
            return;
        }

        const params = {
            table: info.table,
            codeField: info.codeField,
            nameField: info.nameField,
            where: options.where || ''
        };

        Ext.Ajax.request({
            url: dream.util.Common.BASE_URL+'/api/code/table',
            method: 'GET',
            params: params,
            success: function (response) {
                const data = Ext.decode(response.responseText);
                if (options.addDefault) {
                    data.unshift({ CODECD: '', CODENM: options.addDefault });
                }

                const store = Ext.create('Ext.data.Store', {
                    fields: ['CODECD', 'CODENM'],
                    data: data
                });

                combo.setStore(store);
                if (options.defaultValue !== undefined) {
                    combo.setValue(options.defaultValue);
                }
            },
            failure: function () {
                Ext.Msg.alert('오류', '코드 데이터를 불러오는 데 실패했습니다.');
            }
        });
    }



    

});