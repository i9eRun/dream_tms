Ext.define('DreamNalgae.data.FlagCodeList', {
    singleton:true,

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
    },

    getCodeData: function (type) {
        return this.data[type] || null;
    }

});