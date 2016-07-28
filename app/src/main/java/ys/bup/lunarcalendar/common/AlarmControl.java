package ys.bup.lunarcalendar.common;

import ys.bup.lunarcalendar.entity.FavoriteEntity;

/**
 * Created by ys on 2016. 7. 28..
 */
public interface AlarmControl {
    // 앱 재부팅시 다시 등록하기
    void restoreAlarm();

    // 알람 설정
    void setAlarm(FavoriteEntity alarmItem);

    // 알람 취소
    void cancelAlarm(FavoriteEntity object);

    // 알람 체크
    boolean checkAlarm(FavoriteEntity object);

    // 알람 추가
    void insertAlarm(FavoriteEntity alarmItem);

    // 알람 저장하기
    void saveAlarm(int intent_id, String msg, long when, boolean isRepeat);

    // 알람 삭제
    void deleteAlarm(String code);

    // 중복 알람 확인
    boolean existAlarm(String code);
}

