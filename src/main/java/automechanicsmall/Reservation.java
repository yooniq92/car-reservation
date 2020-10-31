package automechanicsmall;

import javax.persistence.*;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

@Entity
@EntityListeners(ReservationListener.class)
@Table(name="Reservation_table")
public class Reservation implements Serializable {

//    @Autowired
//    private EntityManagerFactory entityManagerFactory;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String resvDate;
    private String resvTime;
    private String vehiNo;
    private String stat;

    /* ReservatoinListener에서 처리*/
    // (비기능적 요구조건) 특정 시간에는 2건의 예약을 받을 수 없다.
    // (비기능적 요구조건) 특정 사용자의 기 예약된 건이 있으면 예약 할 수 없다. (예약 1회 초과 불가)

    // 예약 입력
    @PrePersist
    public void onPrePersist(){

        System.out.println("###Reservaton.java - onPrePersist###");

        try{

            if(this.stat.equals("RESERVED")) {
                Reserved reserved = new Reserved();
                BeanUtils.copyProperties(this, reserved);
                reserved.publishAfterCommit();
            }

        }catch(Exception e) {
            //return;
        }

    }

    // 예약 취소
    @PreUpdate
    public void onPreUpdate() {

        // 예약 취소
        if(this.stat.equals("CANCELLED")) {
            System.out.println("###Reservaton.java - PostUpdate###");

            ReservedCancelled reservedCancelled = new ReservedCancelled();
            BeanUtils.copyProperties(this, reservedCancelled);
            reservedCancelled.publishAfterCommit();

            //Following code causes dependency to external APIs
            // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.
            automechanicsmall.external.Repair repair = new automechanicsmall.external.Repair();

            // mappings goes here
            String resvDate = this.getResvDate();
            String resvTime = this.getResvTime();

            // 예약 취소시 Repair에서 먼저 예약 취소 처리 진행 (SAGA req/res)
            // external RepairService.java에서 Repair로 http 전송
            ReservationApplication.applicationContext.getBean(automechanicsmall.external.RepairService.class).cancel(resvDate, resvTime);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getResvDate() {
        return resvDate;
    }

    public void setResvDate(String resvDate) {
        this.resvDate = resvDate;
    }
    public String getResvTime() {
        return resvTime;
    }

    public void setResvTime(String resvTime) {
        this.resvTime = resvTime;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public String getVehiNo() {
        return vehiNo;
    }

    public void setVehiNo(String vehiNo) {
        this.vehiNo = vehiNo;
    }
}
