package automechanicsmall;

import org.springframework.beans.factory.annotation.Autowired;
import javax.persistence.*;

public class ReservationListener {

    @Autowired
    private ReservationRepository reservationRepository;

    @PrePersist
    public void onPrePersistListener(Reservation reservation) throws Exception {

        // 요청한 예약 시간에 기 예약건이 있는지 확인
        try {
            EntityManager em = BeanUtil.getBean(EntityManager.class);

            String queryString = " SELECT COUNT(*) AS CNT \n" +
                    "   FROM Reservation\n" +
                    "  WHERE RESV_DATE = '" + reservation.getResvDate() + "'\n" +
                    "    AND RESV_TIME = '" + reservation.getResvTime() + "'  " +
                    "    AND RESV_TIME <> 'CANCELLED'  ";

            Object map = em.createQuery(queryString).getSingleResult();

            if (Integer.parseInt(map.toString()) > 0) {
                Exception exception = new Exception();
                throw exception; //예외 발생
            }
        }catch (Exception e){
            throw new Exception("예약 요청하신 시간에 기 예약건이 있습니다");
        }finally {
        }

        // 같은 차량 번호로 예약된 건이 있는지 확인
        try {
            EntityManager em = BeanUtil.getBean(EntityManager.class);

            String queryString = " SELECT COUNT(*) AS CNT \n" +
                    "   FROM Reservation\n" +
                    "  WHERE VEHI_NO = '" + reservation.getVehiNo() + "'\n" +
                    "    AND RESV_TIME <> 'CANCELLED'  ";

            Object map = em.createQuery(queryString).getSingleResult();

            if (Integer.parseInt(map.toString()) > 0) {
                Exception exception = new Exception();
                throw exception; //예외 발생
            }
        }catch (Exception e){
            throw new Exception("해당 차량번호로 기 예약된 건이 있습니다.");
        }finally {
        }

    }
}
