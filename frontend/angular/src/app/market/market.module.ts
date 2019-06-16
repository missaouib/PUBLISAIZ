import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MarketComponent } from './market.component';
import { ProductsComponent } from './products/products.component';
import { ProductComponent } from './product/product.component';
import { RouterModule, Routes } from '@angular/router';


const routes: Routes = [
  { path: '', component: ProductsComponent },
  { path: ':link', component: ProductComponent },
  { path: '*', component: MarketComponent },
];


@NgModule({
  declarations: [MarketComponent, ProductsComponent, ProductComponent],
  imports: [
    CommonModule,
    RouterModule.forChild(routes)
  ]
})
export class MarketModule { }
