import { Component, inject, signal } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';
import { ScrollPositionService } from './services/scroll-position.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('frontend');
  // Mantiene el servicio activo a nivel global para restaurar scroll entre pantallas.
  private readonly scrollPositionService = inject(ScrollPositionService);
}
