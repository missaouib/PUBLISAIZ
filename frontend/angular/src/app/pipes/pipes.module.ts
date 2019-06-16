import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SafePipe } from './safe.pipe';
import { NgPipesModule } from 'angular-pipes';
// https://github.com/fknop/angular-pipes

@NgModule({
  declarations: [
    SafePipe,
  ],
  imports: [
    CommonModule,
  ],
  exports: [
    SafePipe,
    NgPipesModule
  ],
  providers: []
})
export class PipesModule { }
