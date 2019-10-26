import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ArticlesComponent } from './articles/articles.component';
import { ArticleComponent } from './article/article.component';
import { Routes, RouterModule } from '@angular/router';
import { PipesModule } from '../pipes/pipes.module';
import { FormsModule } from '@angular/forms';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';
import { PaginationModule } from 'ngx-bootstrap/pagination';
import { NewsComponent } from './news/news.component';

const routes: Routes = [
  { path: '', component: ArticlesComponent },
  { path: 'blog/:link', component: ArticleComponent },
  { path: '*', component: ArticlesComponent },
];

@NgModule({
  declarations: [ArticlesComponent, ArticleComponent, NewsComponent],
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    PipesModule,
    BsDatepickerModule.forRoot(),
    PaginationModule.forRoot(),
    FormsModule
  ],
})
export class BlogModule { }
