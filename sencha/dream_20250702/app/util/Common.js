Ext.define('dream.util.Common', {
    singleton: true,
    cache: {},

    BASE_URL: 'http://190.150.10.103:8080',
    LOGIN_USER: null,
    LOGIN_USER_NAME: null,
    LOGIN_USER_TYPE: null,
    LOGIN_USER_COURSE: null,
    LOGIN_USER_CET_CD: null,
    ENV_MODE: 'dev',
    resetSession: function () {
        this.LOGIN_USER = null;
        this.LOGIN_USER_NAME = null;
        this.LOGIN_USER_TYPE = null;
        this.LOGIN_USER_COURSE = null;
        this.LOGIN_USER_CET_CD = null;
    },

    loadProgram: function(menuId) {
        const contentPanel = Ext.ComponentQuery.query('#mainContentPanel')[0];
        contentPanel.removeAll(true);

        if (typeof menuId === 'undefined') {
            menuId = 'oman1001';
        }

        const folderName = menuId.substring(0,4).toLowerCase();
        //const classNamePart = menuId.charAt(0).toUpperCase() + menuId.slice(1).toLowerCase();
        const classNamePart = menuId.toLowerCase();
        const className = `dream.view.${folderName}.${classNamePart}`;

        Ext.require(className, function() {
            const viewInstance = Ext.create(className);
            contentPanel.add(viewInstance);

        });
    },

    data: {
        '사용':      { codes: ['1', '0'], names: ['사용', '미사용'] },
        '유무':      { codes: ['1', '0'], names: ['유', '무'] },
        '위탁':      { codes: ['1', '0'], names: ['위탁', '위탁안함'] },
        '종료':      { codes: ['1', '0'], names: ['종료', '유지'] },
        '출력':      { codes: ['1', '0'], names: ['출력', '미출력'] },
        '포함':      { codes: ['1', '0'], names: ['포함', '미포함'] },
        '발송':      { codes: ['1', '0'], names: ['발송', '미발송'] },
        '있음':      { codes: ['1', '0'], names: ['있음', '없음'] },
        '지정':      { codes: ['1', '0'], names: ['지정', '미지정'] },
        '입력':      { codes: ['1', '0'], names: ['입력', '미입력'] },
        '계산':      { codes: ['1', '0'], names: ['계산함', '계산안함'] },
        '확인':      { codes: ['1', '0'], names: ['확인', '미확인'] },
        'YN':        { codes: ['1', '0'], names: ['Y', 'N'] },
        '성별':      { codes: ['1', '2'], names: ['남자', '여자'] },
        '예아니요':      { codes: ['1', '0'], names: ['예', '아니요'] },
    },
    /**
     * 코드 값을 텍스트로 변환
     * @param {String} group - 그룹 이름 (예: '사용', '유무')
     * @param {String} code - 코드 값 (예: '1', '0')
     * @returns {String}
     */
    // 사용예시 dream.util.Common.getFlagCodeName('사용', value);
    getFlagCodeName: function(group, code) {
        const groupObj = this.data[group];
        if (!groupObj) return code;

        const idx = groupObj.codes.indexOf(code);
        return idx !== -1 ? groupObj.names[idx] : code;
    },

    getCodeData: function (type) {
        return this.data[type] || null;
    },

    setComboCode: function (com, groupCd, insertAllOption = true) {
        const combo = Ext.ComponentQuery.query(`#${com.id}`)[0];

        if (!combo) {
            console.warn(`'${com.id}'인 콤보박스를 찾을 수 없습니다.`);
            return;
        }

        const store = Ext.create('Ext.data.Store', {
            fields: ['codeCd', 'codeNm'],
            proxy: {
                type: 'ajax',
                url: dream.util.Common.BASE_URL+`/api/code/${groupCd}`,
                params : {
                    userCetCd: dream.util.Common.LOGIN_USER_CET_CD
                },
                reader: { type: 'json' }
            }
        });

        store.on('load', function(store, records) {
            if (insertAllOption) {
                store.insert(0, { codeCd: '', codeNm: '전체' });
                combo.setValue(''); // '전체' 기본값 설정
            } else {
                if (records.length > 0) {
                    combo.setValue(records[0].get('codeCd')); // 첫 번째 코드 기본값 설정
                }
            }
            combo.getPicker().refresh(); // 드롭다운 초기 버그 방지
        });

        combo.setStore(store);
        combo.displayField = 'codeNm';
        combo.valueField = 'codeCd';
        combo.queryMode = 'local';
        combo.editable = false;

        store.load();
    },

    setFlagCombo: function (com, flagType, addName='', defaultValue='') {
        const combo = Ext.ComponentQuery.query(`#${com.id}`)[0];
        if (!combo) {
            console.warn(`콤보박스 ID(${com.id})가 존재하지 않습니다.`);
            return;
        }

        const codeData = dream.util.Common.getCodeData(flagType);
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
            where: options.where || '',
            userCetCd: dream.util.Common.LOGIN_USER_CET_CD
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
    },



    // 그리드 셀에 코드데이터 바인딩하는 공통 함수

    /**
     * 코드 스토어를 반환하거나 새로 생성
     * @param {String} groupCode ex) 'USER_CHK_GB'
     * @returns {Ext.data.Store}
     */
    getStore: function(groupCode) {
        if (this.cache[groupCode]) {
            return this.cache[groupCode];
        }

        const store = Ext.create('Ext.data.Store', {
            fields: ['codeCd', 'codeNm'],
            proxy: {
                type: 'ajax',
                url: dream.util.Common.BASE_URL + '/api/code/' + groupCode,
                reader: { type: 'json' }
            },
            autoLoad: true
        });

        this.cache[groupCode] = store;
        Ext.StoreManager.register(groupCode + '_Store', store);
        return store;
    },


    // 그리드 컬럼에 적용 방법
    // renderer: dream.util.Common.getRenderer('USER_CHK_GB')
    getRenderer: function(groupCode) {
        const store = dream.util.Common.getStore(groupCode);

        console.log("저장된 스토어");
        console.log(store);
        console.log(value);

        return function(value) {
            const rec = store.findRecord('codeCd', value, 0, false, true, true);
            return rec ? rec.get('codeNm') : value;
        };
    },



    // 공통 코드 유틸 함수
    // 코드 데이터를 저장할 맵
    codeMap: {},

    // 특정 그룹코드로 코드 데이터를 저장
    setCodeData: function(groupCd, codeList) {
        this.codeMap[groupCd] = {};
        codeList.forEach(item => {
            this.codeMap[groupCd][item.codeCd] = item.codeNm;
        });
    },

    // 코드명 반환
    getCodeName: function(groupCd, codeCd) {
        if (this.codeMap[groupCd]) {
            return this.codeMap[groupCd][codeCd] || codeCd;
        }
        return codeCd;
    },

    // 렌더러 함수 생성
    createCodeRenderer: function(groupCd, value) {
        const me = this;
        const code_name = me.getCodeName(groupCd, value);
        return code_name;
    },


    // 날짜 변환 공통함수
    parseYmdToDate: function(ymdString) {
        if (!ymdString || ymdString.length !== 8) return null;

        const year = parseInt(ymdString.substring(0, 4), 10);
        const month = parseInt(ymdString.substring(4, 6), 10) - 1;
        const day = parseInt(ymdString.substring(6, 8), 10);

        return new Date(year, month, day);
    },

    setDateFieldValue: function(fieldId, ymdString) {
        const dateField = Ext.ComponentQuery.query(`#${fieldId}`)[0];
        if (dateField) {
            const dateValue = this.parseYmdToDate(ymdString);
            dateField.setValue(dateValue);
        }
    },

    /**
     * Date 객체를 yyyyMMdd 문자열로 변환
     */
    formatDateToYmd: function(date) {
        if (!(date instanceof Date)) return '';

        const year = date.getFullYear();
        const month = ('0' + (date.getMonth() + 1)).slice(-2);
        const day = ('0' + date.getDate()).slice(-2);

        return `${year}${month}${day}`;
    }



});
