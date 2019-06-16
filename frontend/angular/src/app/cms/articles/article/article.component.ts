import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { AngularEditorConfig } from '@kolkov/angular-editor';
import { ArticleService } from 'src/app/_services/article.service';
import { ArticleModel } from 'src/app/_model/article.model';
import { environment } from 'src/environments/environment';
import { Router } from '@angular/router';
@Component({
  selector: 'app-article',
  templateUrl: './article.component.html',
  styleUrls: ['./article.component.css']
})
export class ArticleComponent implements OnInit {

  @Output() event = new EventEmitter<any>();

  link: string;
  @Input()
  article: ArticleModel;

  editorConfig: AngularEditorConfig = {
    editable: true,
    spellcheck: true,
    height: '200px',
    minHeight: '200px',
    translate: 'yes',
    uploadUrl: environment.apiUrl + 'files'
  };

  constructor(private articleService: ArticleService, private router: Router) { }

  public setArticle(art: any) {
    let sufix = '';
    if (art) {
      sufix = 'files?id=' + art.id;
      this.article = art;
      this.editorConfig.uploadUrl = environment.apiUrl + sufix;
    }
    console.log(this.editorConfig.uploadUrl);
    console.log(art);
  }

  ngOnInit() {
    if (!this.article) {
      this.article = {};
    }
  }

  edit() {
    this.articleService.postArticle(this.article).subscribe(r => {
      this.article = r;
      this.event.emit(this.article);
      this.router.navigate(['/cms/articles/' + r.link]);
    });
  }
}
