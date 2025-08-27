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
        this.LOGIN_USER_ID = null;
        this.LOGIN_USER_NAME = null;
        this.LOGIN_USER_TYPE = null;
        this.LOGIN_USER_COURSE = null;
        this.LOGIN_USER_CET_CD = null;
    },

    loadProgram: function(menuId, menuNm) {
        const contentPanel = Ext.ComponentQuery.query('#mainContentPanel')[0];
        contentPanel.removeAll(true);

        if (typeof menuId === 'undefined') {
            menuId = 'oman1001';
        }

        dream.util.Common.currentMenuNm = menuNm; 

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
        '예아니요':  { codes: ['1', '0'], names: ['예', '아니요'] },
        '정반':  { codes: ['1', '0'], names: ['반품', '정품'] },
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

        store.on('load', function(store, records, successful) {
            if (insertAllOption) {
                store.insert(0, { codeCd: '', codeNm: '전체' });
                combo.setValue('');
            } else {
                if (successful && Array.isArray(records) && records.length > 0) {
                    combo.setValue(records[0].get('codeCd'));
                }
            }
            combo.getPicker().refresh();
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

        combo.displayField = 'codeNm';
        combo.valueField = 'codeCd';
        combo.queryMode = 'local';
        combo.editable = false;
        
        combo.setStore(store);
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
            SOSOK_NM:     { table: 'TMS_CUST',         codeField: 'CUST_CD',       nameField: 'CUST_NM' },
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
    },

    /**
     * @param {Ext.form.Panel} form - 대상 form
     * @param {String[]} fieldOrder - 필드 식별자 배열 (itemId, reference 또는 name)
     * 사용 예시 : dream.util.Common.enableEnterKeyNavigation(form, ['ordNoField', 'ordDtField', 'labelDtField', 'poNoField', 'publisherField']);
     * const fields = [];
     * dream.util.Common.enableEnterKeyNavigation(form, fields);
     */
    enableEnterKeyNavigation: function (form, fieldOrder) {
        fieldOrder.forEach((fieldKey, index) => {
            const field = form.down('#' + fieldKey) || form.lookupReference(fieldKey) || form.down('[name=' + fieldKey + ']');
            if (!field) return;

            field.on('specialkey', function (f, e) {
                if (e.getKey() === e.ENTER) {
                    e.preventDefault();
                    const nextFieldKey = fieldOrder[index + 1];
                    if (nextFieldKey) {
                        const nextField = form.down('#' + nextFieldKey) || form.lookupReference(nextFieldKey) || form.down('[name=' + nextFieldKey + ']');
                        if (nextField) {
                            nextField.focus();
                        }
                    }
                }
            });
        });
    },

    /**
     * 공통 코드 데이터를 서버에서 조회하여 저장하는 함수
     * @param {String} codeId - 코드 ID (예: '257')
     * @param {Function} [callback] - 성공 시 실행할 콜백 함수 (선택)
     */
    loadCode: function (codeId, callback) {
        Ext.Ajax.request({
            url: dream.util.Common.BASE_URL + '/api/code/' + codeId,
            method: 'GET',
            success: function(response) {
                const codeList = Ext.decode(response.responseText);
                dream.util.Common.setCodeData(codeId, codeList);

                if (typeof callback === 'function') {
                    callback(codeList);
                }
            },
            failure: function(response) {
                Ext.Msg.alert('오류', '코드 [' + codeId + '] 조회 실패');
            }
        });
    },



    // 브라우저단에서 csv 형태로 서버통신없이 파일생성. 서식지정안돼 사용안함.
    exportGridToExcel_csv: function (grid, fileName = '엑셀다운로드') {
        const store = grid.getStore();
        const columns = grid.getColumnManager().getColumns()
            .filter(col => col.dataIndex && !col.hidden);

        // ✅ 헤더 추출
        const headers = columns.map(col => col.text);

        // ✅ 데이터 추출
        const data = store.getRange().map(record => {
            return columns.map(col => {
                let value = record.get(col.dataIndex);
                // renderer 적용 (선택)
                if (typeof col.renderer === 'function') {
                    const meta = {};
                    value = col.renderer.call(col.scope || this, value, meta, record);
                }
                return value;
            });
        });

        // ✅ SheetJS 형식 변환
        const worksheetData = [headers, ...data]; // 첫 행은 헤더
        const worksheet = XLSX.utils.aoa_to_sheet(worksheetData);
        const workbook = XLSX.utils.book_new();
        XLSX.utils.book_append_sheet(workbook, worksheet, "Sheet1");

        // ✅ 파일명에 날짜 붙이기
        const today = Ext.Date.format(new Date(), 'Ymd');
        const fullFileName = `${fileName}_${today}.xlsx`;

        XLSX.writeFile(workbook, fullFileName);
    },



    // 자바스크립트 sheetjs 이용한 엑셀 공통함수. 무료버전은 서식지원이 안돼 사용안함.
    exportGridToExcel_sheetjs: function (grid, fileName = '엑셀다운로드') {
        const store = grid.getStore();
        const columns = grid.getColumnManager().getColumns()
            .filter(col => col.dataIndex && !col.hidden);

        const headers = columns.map(col => col.text);
        const data = store.getRange().map(record =>
            columns.map(col => {
                let value = record.get(col.dataIndex);
                if (typeof col.renderer === 'function') {
                    const meta = {};
                    value = col.renderer.call(col.scope || this, value, meta, record);
                }
                return value;
            })
        );

        const worksheetData = [headers, ...data];
        const worksheet = XLSX.utils.aoa_to_sheet(worksheetData);

        // ✅ 열 너비 자동 계산
        const colWidths = worksheetData[0].map((_, colIdx) => {
            const maxLength = worksheetData.reduce((max, row) => {
                const val = row[colIdx] != null ? row[colIdx].toString() : '';
                return Math.max(max, val.length);
            }, 10);
            return { wch: maxLength + 2 }; // padding
        });
        worksheet['!cols'] = colWidths;

        // ✅ 셀 스타일 지정 (강화된 스타일 적용)
        const borderStyle = {
            top:    { style: 'thin', color: { rgb: "000000" } },
            bottom: { style: 'thin', color: { rgb: "000000" } },
            left:   { style: 'thin', color: { rgb: "000000" } },
            right:  { style: 'thin', color: { rgb: "000000" } }
        };

        const range = XLSX.utils.decode_range(worksheet['!ref']);
        for (let R = range.s.r; R <= range.e.r; ++R) {
            for (let C = range.s.c; C <= range.e.c; ++C) {
                const cellAddress = XLSX.utils.encode_cell({ r: R, c: C });
                const cell = worksheet[cellAddress] || (worksheet[cellAddress] = { t: 's', v: '' });

                // 공통 스타일: 중앙 정렬 + 보더
                cell.s = {
                    alignment: { horizontal: 'center', vertical: 'center' },
                    border: borderStyle
                };

                if (R === 0) {
                    // 헤더만: 회색 배경 + 볼드체
                    cell.s.fill = {
                        patternType: "solid",
                        fgColor: { rgb: "D9D9D9" }
                    };
                    cell.s.font = {
                        bold: true,
                        color: { rgb: "000000" }
                    };
                }
            }
        }

        // ✅ 워크북 생성 및 다운로드
        const wb = XLSX.utils.book_new();
        XLSX.utils.book_append_sheet(wb, worksheet, 'Sheet1');

        const today = Ext.Date.format(new Date(), 'Ymd');
        const fullFileName = `${fileName}_${today}.xlsx`;

        // 다운로드
        XLSX.writeFile(wb, fullFileName, { cellStyles: true });
    },


    exportGridToExcel(grid) {
        const store = grid.getStore();
        const columns = grid.getColumns().filter(col => col.dataIndex && !col.hidden);

        // 1. 데이터가 없는 경우 예외처리
        if (store.getCount() === 0) {
            Ext.Msg.alert('알림', '엑셀로 내보낼 데이터가 없습니다.');
            return;
        }

        // 1. 헤더 정보 구성
        const headers = columns.map(col => ({
            header: col.text,
            dataIndex: col.dataIndex
        }));

        // 2. 데이터 구성 (렌더러 처리 포함)
        const data = store.getRange().map(record => {
            const rowData = {};
            columns.forEach(col => {
                const rawValue = record.get(col.dataIndex);
                if (col.renderer) {
                    rowData[col.dataIndex] = col.renderer(rawValue, {}, record);
                } else {
                    rowData[col.dataIndex] = rawValue;
                }
            });
            return rowData;
        });

        // 3. 파일명: 오늘날짜_메뉴이름.xlsx
        const today = Ext.Date.format(new Date(), 'Ymd'); // 예: 20250731
        const menuNm = dream.util.Common.currentMenuNm || '메뉴이름없음';
        const fileName = `${today}_${menuNm}.xlsx`;

        // 4. 엑셀 다운로드 요청
        Ext.Ajax.request({
            url: dream.util.Common.BASE_URL + '/system/exceldownload',
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            binary: true,
            jsonData: {
                fileName: fileName,
                headers: headers,
                data: data
            },
            success: function (response) {
                const blob = new Blob([response.responseBytes], {
                    type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
                });

                const downloadUrl = window.URL.createObjectURL(blob);
                const a = document.createElement('a');
                a.href = downloadUrl;
                a.download = fileName;
                document.body.appendChild(a);
                a.click();
                document.body.removeChild(a);
                window.URL.revokeObjectURL(downloadUrl);
            },
            failure: function () {
                Ext.Msg.alert('엑셀 다운로드 실패', '서버와 통신 중 오류가 발생했습니다.');
            }
        });
    }


});
