export interface ArticleModel {
    id?: string;
    title?: string;
    content?: string;
    author?: string;
    tags?: string[];
    comments?: string[];
    link?: string;
    image?: string;
    hide?: boolean;
    created?: string;
    edited?: string;
}
