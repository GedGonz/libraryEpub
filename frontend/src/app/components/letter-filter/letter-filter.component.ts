import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';

const ALL_LETTERS: string[] = Array.from({ length: 26 }, (_, i) => String.fromCharCode(65 + i));

@Component({
  selector: 'app-letter-filter',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './letter-filter.component.html',
  styleUrl: './letter-filter.component.css',
})
export class LetterFilterComponent {
  @Input() activeLetter: string = 'ALL';
  @Output() letterSelected = new EventEmitter<string>();

  readonly letters = ALL_LETTERS;

  select(letter: string) {
    this.letterSelected.emit(letter);
  }
}

