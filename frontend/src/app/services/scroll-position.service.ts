import { Injectable } from '@angular/core';
import { NavigationEnd, NavigationStart, Router } from '@angular/router';
import { ViewportScroller } from '@angular/common';

@Injectable({ providedIn: 'root' })
export class ScrollPositionService {
  private readonly storageKey = 'scroll-positions-v1';
  private readonly positions = new Map<string, [number, number]>();

  constructor(
    private readonly router: Router,
    private readonly viewportScroller: ViewportScroller,
  ) {
    this.loadFromSessionStorage();

    this.router.events.subscribe((event) => {
      if (event instanceof NavigationStart) {
        this.saveCurrentPosition();
        return;
      }

      if (event instanceof NavigationEnd) {
        this.restorePosition(event.urlAfterRedirects);
      }
    });

    window.addEventListener('beforeunload', () => {
      this.saveCurrentPosition();
    });
  }

  private saveCurrentPosition() {
    const key = this.normalizeUrl(this.router.url);
    this.positions.set(key, this.viewportScroller.getScrollPosition());
    this.persistToSessionStorage();
  }

  private restorePosition(url: string) {
    const key = this.normalizeUrl(url);
    const target = this.positions.get(key) ?? [0, 0];

    // Reintentamos durante el primer render para rutas con contenido async.
    const retriesMs = [0, 60, 180, 320];
    for (const delay of retriesMs) {
      setTimeout(() => this.viewportScroller.scrollToPosition(target), delay);
    }
  }

  private normalizeUrl(url: string): string {
    return url.split('#')[0];
  }

  private persistToSessionStorage() {
    try {
      const asObject = Object.fromEntries(this.positions.entries());
      sessionStorage.setItem(this.storageKey, JSON.stringify(asObject));
    } catch {
      // Ignoramos errores de quota/privacidad para no afectar navegación.
    }
  }

  private loadFromSessionStorage() {
    try {
      const raw = sessionStorage.getItem(this.storageKey);
      if (!raw) return;

      const parsed = JSON.parse(raw) as Record<string, unknown>;
      for (const [key, value] of Object.entries(parsed)) {
        if (Array.isArray(value) && value.length === 2) {
          const x = Number(value[0]);
          const y = Number(value[1]);
          if (Number.isFinite(x) && Number.isFinite(y)) {
            this.positions.set(key, [x, y]);
          }
        }
      }
    } catch {
      // Si hay formato inválido, simplemente iniciamos sin cache.
    }
  }
}

