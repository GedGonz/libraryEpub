import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class DeviceIdService {
  private readonly storageKey = 'device-id-v1';
  private runtimeFallbackId: string | null = null;

  getOrCreate(): string {
    try {
      const existing = localStorage.getItem(this.storageKey);
      if (existing && this.isValid(existing)) {
        return existing;
      }

      const generated = this.generateUuid();
      localStorage.setItem(this.storageKey, generated);
      return generated;
    } catch {
      // Fallback para entornos restringidos: ID estable solo en memoria de ejecución.
      if (!this.runtimeFallbackId) {
        this.runtimeFallbackId = this.generateUuid();
      }
      return this.runtimeFallbackId;
    }
  }

  private isValid(value: string): boolean {
    return /^[a-zA-Z0-9-]{8,64}$/.test(value);
  }

  private generateUuid(): string {
    const c = globalThis.crypto as Crypto | undefined;
    if (c && typeof c.randomUUID === 'function') {
      return c.randomUUID();
    }

    // Fallback RFC4122 v4-like para navegadores sin randomUUID.
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, (ch) => {
      const rand = Math.floor(Math.random() * 16);
      const value = ch === 'x' ? rand : (rand & 0x3) | 0x8;
      return value.toString(16);
    });
  }
}

