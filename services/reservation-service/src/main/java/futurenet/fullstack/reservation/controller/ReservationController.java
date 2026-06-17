package futurenet.fullstack.reservation.controller;

import futurenet.fullstack.reservation.dto.ReservationResponse;
import futurenet.fullstack.reservation.service.ReservationService;
import futurenet.fullstack.reservation.support.ReservationUserResolver;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

  private final ReservationService reservationService;
  private final ReservationUserResolver reservationUserResolver;

  @GetMapping
  public String list(Model model) {
    Long currentUserId = reservationUserResolver.currentUserId();
    List<ReservationResponse> reservations =
        reservationService.findAllByUserId(currentUserId);

    model.addAttribute("reservations", reservations);

    return "reservations/list";
  }

  @GetMapping("/new")
  public String newForm(Model model) {
    Long currentUserId = reservationUserResolver.currentUserId();

    model.addAttribute("mode", "create");
    model.addAttribute("form", ReservationForm.empty(currentUserId));

    return "reservations/form";
  }

  @PostMapping
  public String create(
      @Valid @ModelAttribute("form") ReservationForm form,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes
  ) {
    form.setUserId(reservationUserResolver.currentUserId());

    if (bindingResult.hasErrors()) {
      model.addAttribute("mode", "create");
      return "reservations/form";
    }

    reservationService.create(form.toCreateRequest());
    redirectAttributes.addFlashAttribute("message", "예약 메일을 등록했어요.");

    return redirectToList();
  }

  @GetMapping("/{emailId}/edit")
  public String editForm(
      @PathVariable Long emailId,
      Model model
  ) {
    Long currentUserId = reservationUserResolver.currentUserId();
    ReservationResponse reservation =
        reservationService.findById(emailId, currentUserId);

    model.addAttribute("mode", "edit");
    model.addAttribute("emailId", emailId);
    model.addAttribute("form", ReservationForm.from(reservation));

    return "reservations/form";
  }

  @PostMapping("/{emailId}/edit")
  public String update(
      @PathVariable Long emailId,
      @Valid @ModelAttribute("form") ReservationForm form,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes
  ) {
    form.setUserId(reservationUserResolver.currentUserId());

    if (bindingResult.hasErrors()) {
      model.addAttribute("mode", "edit");
      model.addAttribute("emailId", emailId);
      return "reservations/form";
    }

    try {
      reservationService.update(emailId, form.getUserId(), form.toUpdateRequest());
      redirectAttributes.addFlashAttribute("message", "예약 메일을 수정했어요.");

      return redirectToList();
    } catch (IllegalStateException ex) {
      bindingResult.reject("reservation.update.failed", ex.getMessage());
      model.addAttribute("mode", "edit");
      model.addAttribute("emailId", emailId);
      return "reservations/form";
    }
  }

  @PostMapping("/{emailId}/cancel")
  public String cancel(
      @PathVariable Long emailId,
      RedirectAttributes redirectAttributes
  ) {
    Long currentUserId = reservationUserResolver.currentUserId();

    try {
      reservationService.cancel(emailId, currentUserId);
      redirectAttributes.addFlashAttribute("message", "예약 메일을 취소했어요.");
    } catch (IllegalStateException ex) {
      redirectAttributes.addFlashAttribute("error", ex.getMessage());
    }

    return redirectToList();
  }

  @PostMapping("/{emailId}/delete")
  public String delete(
      @PathVariable Long emailId,
      RedirectAttributes redirectAttributes
  ) {
    Long currentUserId = reservationUserResolver.currentUserId();

    try {
      reservationService.delete(emailId, currentUserId);
      redirectAttributes.addFlashAttribute("message", "취소된 예약 메일을 삭제했어요.");
    } catch (IllegalStateException ex) {
      redirectAttributes.addFlashAttribute("error", ex.getMessage());
    }

    return redirectToList();
  }

  private String redirectToList() {
    return "redirect:/reservations";
  }
}
