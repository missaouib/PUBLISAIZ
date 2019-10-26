import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ArticleComponent } from './articles/article/article.component';
import { ArticlesComponent } from './articles/articles.component';
import { FilesComponent } from './files/files.component';
import { XlsComponent } from './xls/xls.component';
import { SellComponent } from './sell/sell.component';
import { PurchasedComponent } from './purchased/purchased.component';
import { SettingsComponent } from './settings/settings.component';
import { ProfileComponent } from './profile/profile.component';
import { RouterModule } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { AngularEditorModule } from '@kolkov/angular-editor';
import { FormsModule } from '@angular/forms';
import { PipesModule } from '../pipes/pipes.module';
import { PaginationModule } from 'ngx-bootstrap/pagination';
import { EditorModule } from '@tinymce/tinymce-angular';


const routes = [
  { path: 'article', component: ArticleComponent },
  { path: 'article/:link', component: ArticleComponent },
  { path: 'articles', component: ArticlesComponent },
  { path: 'articles/:link', component: ArticlesComponent },
  { path: 'files', component: FilesComponent },
  { path: 'xls', component: XlsComponent },
  { path: 'sell', component: SellComponent },
  { path: 'purchased', component: PurchasedComponent },
  { path: 'settings', component: SettingsComponent },
  { path: 'profile', component: ProfileComponent },
  { path: '**', component: ProfileComponent }
];
@NgModule({
  declarations: [ ArticleComponent,
                  ArticlesComponent,
                  FilesComponent,
                  XlsComponent,
                  SellComponent,
                  PurchasedComponent,
                  SettingsComponent,
                  ProfileComponent],
  imports: [
    CommonModule,
    FormsModule,
    HttpClientModule,
    AngularEditorModule,
    RouterModule.forChild(routes),
    PaginationModule.forRoot(),
    PipesModule,
    EditorModule
  ]
})
export class CmsModule { }
