
package automechanicsmall.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@FeignClient(name="repair", url="${api.repair.url}")
public interface ReceiptService {

    //PUT 정상 작동
    //@RequestMapping(method= RequestMethod.PUT, path="/receipts/{id}")
    ////@PutMapping(path="/receipts/{id}")
    //public void cancel(@RequestBody Receipt receipt, @PathVariable("id") int id);

    //GET 정상 작동1
    //@GetMapping("/receipts/{id}")
    //public void cancel(@RequestParam("rcptDate") String input, @PathVariable("id") int id);

    // 예약 취소
    @GetMapping("/receipts/cancel")
    void cancel(@RequestParam("rcptDate") String rcptDate, @RequestParam("rcptTime") String rcptTime);

}