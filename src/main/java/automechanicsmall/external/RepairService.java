
package automechanicsmall.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@FeignClient(name="repair", url="${api.repair.url}")
public interface RepairService {

    //PUT 정상 작동
    //@RequestMapping(method= RequestMethod.PUT, path="/repairs/{id}")
    ////@PutMapping(path="/repairs/{id}")
    //public void cancel(@RequestBody Repair repair, @PathVariable("id") int id);

    //GET 정상 작동1
    //@GetMapping("/repairs/{id}")
    //public void cancel(@RequestParam("rcptDate") String input, @PathVariable("id") int id);

    // 예약 취소
    @GetMapping("/repairs/cancel")
    void cancel(@RequestParam("rcptDate") String rcptDate, @RequestParam("rcptTime") String rcptTime);

}