import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthorsComponent } from './authors.component';
import { AuthorComponent } from './author/author.component';
import { RouterModule } from '@angular/router';

const routes = [
  { path: '', component: AuthorsComponent },
  { path: ':link', component: AuthorComponent },
  { path: '**', component: AuthorsComponent }
];
@NgModule({
  declarations: [AuthorsComponent, AuthorComponent],
  imports: [
    CommonModule,
    RouterModule.forChild(routes)
  ]
})
export class AuthorsModule { }
