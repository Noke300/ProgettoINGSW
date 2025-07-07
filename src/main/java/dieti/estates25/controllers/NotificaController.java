package dieti.estates25.controllers;

import dieti.estates25.service.NotificaService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/notifiche")
public class NotificaController {

    private final NotificaService notificaService;

    public NotificaController(NotificaService notificaService) {
        this.notificaService = notificaService;
    }

    @GetMapping("/stream")
    public SseEmitter ascoltaNotifiche(@RequestParam String userId) {
        return notificaService.registraUtente(userId);
    }
}

